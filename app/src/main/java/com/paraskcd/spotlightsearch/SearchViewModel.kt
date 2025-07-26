package com.paraskcd.spotlightsearch

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.icons.Calculate
import com.paraskcd.spotlightsearch.providers.AppRepositoryProvider
import com.paraskcd.spotlightsearch.providers.ContactSearchProvider
import com.paraskcd.spotlightsearch.providers.FileSearchProvider
import com.paraskcd.spotlightsearch.providers.GoogleSuggestionProvider
import com.paraskcd.spotlightsearch.providers.PlayStoreSearchProvider
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import android.os.Environment
import android.provider.Settings
import androidx.compose.material.icons.filled.Warning
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.providers.MLKitTranslationProvider
import com.paraskcd.spotlightsearch.providers.MathEvaluationProvider
import com.paraskcd.spotlightsearch.providers.SettingsSearchProvider
import com.paraskcd.spotlightsearch.providers.SpellCheckerProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val appRepository: AppRepositoryProvider,
    private val suggestionProvider: GoogleSuggestionProvider,
    private val playStoreSearchProvider: PlayStoreSearchProvider,
    private val contactSearchProvider: ContactSearchProvider,
    private val fileSearchProvider: FileSearchProvider,
    private val mathEvaluationProvider: MathEvaluationProvider,
    private val mlKitTranslationProvider: MLKitTranslationProvider,
    private val spellCheckerProvider: SpellCheckerProvider,
    private val settingsSearchProvider: SettingsSearchProvider
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private var searchJob: Job? = null

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()

    init {
        viewModelScope.launch {
            spellCheckerProvider.init()
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        updateResults()
    }

    fun onSearch(queryText: String) {
        val firstMatch = _results.value.firstOrNull {
            !it.isHeader && when (it.searchResultType) {
                SearchResultType.APP,
                SearchResultType.CONTACT,
                SearchResultType.FILE,
                SearchResultType.SETTINGS-> true
                else -> false
            }
        }

        if (firstMatch != null) {
            firstMatch.onClick()
        } else {
            var encodedQuery = Uri.encode(query.value.trim())
            if (encodedQuery.isEmpty()) {
                encodedQuery = Uri.encode(queryText.trim())
            }
            val uri = Uri.parse("https://www.google.com/search?q=$encodedQuery")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private fun updateResults() {
        val query = _query.value
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _results.value = emptyList()
                return@launch
            }

            val needsFilePermission = !Environment.isExternalStorageManager()
            val needsContactPermission = contactSearchProvider.requiresPermission()

            val permissionPrompt = buildList {
                if (needsFilePermission || needsContactPermission) {
                    add(SearchResult(title = "Permissions", isHeader = true, onClick = {}))
                }
                if (needsFilePermission) {
                    add(SearchResult(
                        title = "Allow file access",
                        subtitle = "Required to search local files",
                        iconVector = Icons.Filled.Warning,
                        onClick = {
                            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                                data = "package:${context.packageName}".toUri()
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        },
                        searchResultType = SearchResultType.PERMISSION
                    ))
                }
                if (needsContactPermission) {
                    add(SearchResult(
                        title = "Allow contact access",
                        subtitle = "Required to search contacts",
                        iconVector = Icons.Filled.Warning,
                        onClick = {
                            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = "package:${context.packageName}".toUri()
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        },
                        searchResultType = SearchResultType.PERMISSION
                    ))
                }
            }
            _results.update { permissionPrompt + it }

            val cleanQuery = query.trim()
            val suggestions = spellCheckerProvider.suggest(cleanQuery)
            val first = suggestions.firstOrNull()
            if (!first.isNullOrBlank() && !first.equals(cleanQuery, ignoreCase = true)) {
                val header = SearchResult(title = "Dictionary", isHeader = true, onClick = {})
                val suggestionResult = SearchResult(
                    title = "Did you mean...?",
                    subtitle = first,
                    iconVector = Icons.Filled.Search,
                    onClick = { onQueryChanged(first) },
                    searchResultType = SearchResultType.SUGGESTION,
                    hasTextChangeFlag = true
                )
                _results.update { listOf(header, suggestionResult) + it }
            }

            val settingsResults = settingsSearchProvider.searchSettings(cleanQuery)
            if (settingsResults.isNotEmpty()) {
                _results.update { settingsResults + it }
            }

            // Math/Date/Unit/Temp evaluation
            val mathResult = mathEvaluationProvider.evaluate(query)
            mathResult?.let {
                _results.update { prev ->
                    prev + listOf(
                        SearchResult(title = "Calculator", isHeader = true, onClick = {}),
                        it
                    )
                }
            }

            val translationDeferred = async { mlKitTranslationProvider.translate(query) }
            val appDeferred = async { appRepository.searchInstalledApp(query) }
            val contactDeferred = async { contactSearchProvider.searchContacts(query) }
            val fileDeferred = async { fileSearchProvider.searchFiles(query) }
            val suggestionDeferred = async { suggestionProvider.fetchSuggestions(query) }
            val playStoreDeferred = async { playStoreSearchProvider.getPlayStoreSearchItem(query) }

            appDeferred.invokeOnCompletion {
                appDeferred.getCompletedOrNull()?.takeIf { it.isNotEmpty() }?.let {
                    val list = listOf(SearchResult(title = "Apps", isHeader = true, onClick = {})) + it
                    _results.update { prev -> prev + list }
                }
            }

            contactDeferred.invokeOnCompletion {
                contactDeferred.getCompletedOrNull()?.takeIf { it.isNotEmpty() }?.let {
                    val list = listOf(SearchResult(title = "Contacts", isHeader = true, onClick = {})) + it
                    _results.update { prev -> prev + list }
                }
            }

            fileDeferred.invokeOnCompletion {
                fileDeferred.getCompletedOrNull()?.takeIf { it.isNotEmpty() }?.let {
                    val list = listOf(SearchResult(title = "Files", isHeader = true, onClick = {})) + it
                    _results.update { prev -> prev + list }
                }
            }

            val webResults = mutableListOf<SearchResult>()
            webResults.add(SearchResult(title = "Web", isHeader = true, onClick = {}))
            webResults.add(SearchResult(
                title = "Search \"$query\" on the web",
                subtitle = "Web Search",
                iconVector = Icons.Filled.Search,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://www.google.com/search?q=${Uri.encode(query)}".toUri()
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                },
                searchResultType = SearchResultType.WEB
            ))
            _results.update { it + webResults }

            translationDeferred.invokeOnCompletion {
                translationDeferred.getCompletedOrNull()?.let {
                    val list = listOf(SearchResult(title = "Translation", isHeader = true, onClick = {})) + it
                    _results.update { prev -> prev + list }
                }
            }

            suggestionDeferred.await().let { suggestions ->
                val sorted = suggestions.mapIndexed { _, suggestion ->
                    val isCalc = suggestion.trim().startsWith("=")
                    SearchResult(
                        title = if (isCalc) "$query $suggestion" else suggestion,
                        subtitle = if (isCalc) "Calculator" else "Suggestion",
                        iconVector = if (isCalc) Calculate else Icons.Filled.Search,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = "https://www.google.com/search?q=${Uri.encode(suggestion)}".toUri()
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        },
                        searchResultType = if (isCalc) SearchResultType.CALCULATOR else SearchResultType.SUGGESTION
                    )
                }

                val calculatorItem = sorted.firstOrNull { it.subtitle == "Calculator" }
                val otherSuggestions = sorted.filter { it.subtitle != "Calculator" }

                val extra = mutableListOf<SearchResult>()
                calculatorItem?.let {
                    extra.add(SearchResult(title = "Calculator", isHeader = true, onClick = {}))
                    extra.add(it)
                }

                if (calculatorItem != null) {
                    // Remove previous local calculator results
                    _results.update { prev -> prev.filterNot { it.searchResultType == SearchResultType.CALCULATOR || (it.isHeader && it.title == "Calculator") } }
                    extra.clear()
                    extra.add(SearchResult(title = "Calculator", isHeader = true, onClick = {}))
                    extra.add(calculatorItem)
                }

                if (appDeferred.getCompletedOrNull().isNullOrEmpty() &&
                    contactDeferred.getCompletedOrNull().isNullOrEmpty() &&
                    calculatorItem == null
                ) {
                    if (otherSuggestions.isNotEmpty()) {
                        extra.add(SearchResult(title = "Suggestions", isHeader = true, onClick = {}))
                        extra.addAll(otherSuggestions)
                    }
                }

                _results.update { it + extra }
            }

            playStoreDeferred.await()?.let {
                val play = listOf(SearchResult(title = "Play Store", isHeader = true, onClick = {}), it)
                _results.update { it + play }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun <T> Deferred<T>.getCompletedOrNull(): T? =
        if (isCompleted && !isCancelled) getCompleted() else null
}