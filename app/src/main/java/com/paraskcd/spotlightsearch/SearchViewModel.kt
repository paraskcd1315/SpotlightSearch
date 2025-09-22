package com.paraskcd.spotlightsearch

import android.app.SearchManager
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.icons.Calculate
import com.paraskcd.spotlightsearch.providers.AppRepositoryProvider
import com.paraskcd.spotlightsearch.providers.ContactSearchProvider
import com.paraskcd.spotlightsearch.providers.GoogleSuggestionProvider
import com.paraskcd.spotlightsearch.providers.MultipleSearchProvider
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
import androidx.compose.material.icons.filled.Warning
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import com.paraskcd.spotlightsearch.data.entities.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.data.repo.AppUsageRepository
import com.paraskcd.spotlightsearch.data.repo.GlobalSearchConfigRepository
import com.paraskcd.spotlightsearch.enums.SearchResultDisplayMode
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.providers.MLKitTranslationProvider
import com.paraskcd.spotlightsearch.providers.MathEvaluationProvider
import com.paraskcd.spotlightsearch.providers.SettingsSearchProvider
import com.paraskcd.spotlightsearch.providers.SpellCheckerProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SearchViewModel @Inject constructor(
    appUsageRepository: AppUsageRepository,
    @param:ApplicationContext val context: Context,
    private val appRepository: AppRepositoryProvider,
    private val suggestionProvider: GoogleSuggestionProvider,
    private val multipleSearchProvider: MultipleSearchProvider,
    private val contactSearchProvider: ContactSearchProvider,
    private val mathEvaluationProvider: MathEvaluationProvider,
    private val mlKitTranslationProvider: MLKitTranslationProvider,
    private val spellCheckerProvider: SpellCheckerProvider,
    private val settingsSearchProvider: SettingsSearchProvider,
    private val globalConfigRepo: GlobalSearchConfigRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private var searchJob: Job? = null

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()

    private val topApps: StateFlow<List<SearchResult>> =
        appUsageRepository.observeTop(limit = 5)
            .map { entities ->
                entities.mapNotNull { e ->
                    appRepository.getCachedApp(e.packageName)
                        ?.copy(
                            displayMode = SearchResultDisplayMode.COMPACT,
                            searchResultType = SearchResultType.APP_FREQUENT
                        )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val globalConfig = globalConfigRepo.config
        .stateIn(viewModelScope, SharingStarted.Eagerly, GlobalSearchConfigEntity())

    init {
        viewModelScope.launch {
            globalConfigRepo.ensure()
        }
        viewModelScope.launch {
            spellCheckerProvider.init()
        }
        viewModelScope.launch {
            topApps.collect {
                if (_query.value.isBlank()) updateResults()
            }
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
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, queryText.trim())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private fun updateResults() {
        val cfg = globalConfig.value

        val query = _query.value.trim()
        if (query.isEmpty()) {
            val list = mutableListOf<SearchResult>()
            if (topApps.value.isNotEmpty()) {
                list += SearchResult(
                    title = "Frequently used",
                    onClick = {},
                    isHeader = true
                )
                list += topApps.value
            }

            _results.value = list
            return
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _results.value = emptyList()
                return@launch
            }

            val needsContactPermission = contactSearchProvider.requiresPermission()

            val permissionPrompt = buildList {
                if (needsContactPermission) {
                    add(SearchResult(title = "Permissions", isHeader = true, onClick = {}))
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
            _results.value = permissionPrompt

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
            val appDeferred = if (cfg.appsEnabled) async { appRepository.searchInstalledApp(query) } else null
            val contactDeferred = if (cfg.contactsEnabled) async { contactSearchProvider.searchContacts(query) } else null
            val suggestionDeferred = if (cfg.webSuggestionsEnabled) async { suggestionProvider.fetchSuggestions(query) } else null
            val multiSearchDeferred = async { multipleSearchProvider.getSearchAppItems(query) }

            appDeferred?.invokeOnCompletion {
                appDeferred.getCompletedOrNull()?.takeIf { it.isNotEmpty() }?.let {
                    val list = listOf(SearchResult(title = "Apps", isHeader = true, onClick = {})) + it
                    _results.update { prev -> prev + list }
                }
            }

            contactDeferred?.invokeOnCompletion {
                contactDeferred.getCompletedOrNull()?.takeIf { it.isNotEmpty() }?.let {
                    val list = listOf(SearchResult(title = "Contacts", isHeader = true, onClick = {})) + it
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
                    val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                        putExtra(SearchManager.QUERY, query)
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

            suggestionDeferred?.await().let { suggestions ->
                val sorted = suggestions?.mapIndexed { _, suggestion ->
                    val isCalc = suggestion.trim().startsWith("=")
                    SearchResult(
                        title = if (isCalc) "$query $suggestion" else suggestion,
                        subtitle = if (isCalc) "Calculator" else "Suggestion",
                        iconVector = if (isCalc) Calculate else Icons.Filled.Search,
                        onClick = {
                            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                                putExtra(SearchManager.QUERY, suggestion.trim())
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        },
                        searchResultType = if (isCalc) SearchResultType.CALCULATOR else SearchResultType.SUGGESTION
                    )
                }

                val calculatorItem = sorted?.firstOrNull { it.subtitle == "Calculator" }
                val otherSuggestions = sorted?.filter { it.subtitle != "Calculator" }

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

                if (appDeferred?.getCompletedOrNull().isNullOrEmpty() &&
                    contactDeferred?.getCompletedOrNull().isNullOrEmpty() &&
                    calculatorItem == null
                ) {
                    if (otherSuggestions?.isNotEmpty() == true) {
                        extra.add(SearchResult(title = "Suggestions", isHeader = true, onClick = {}))
                        extra.addAll(otherSuggestions)
                    }
                }

                _results.update { it + extra }
            }

            multiSearchDeferred?.await().takeIf { it?.isNotEmpty() == true }?.let { list ->
                _results.update { prev -> prev + list }
            }
        }
    }

    fun getAppIcons(pkg: String, dynamicColor: Int? = null) = appRepository.getAppIcon(packageName = pkg, applyDynamicColoring = true, dynamicColor = dynamicColor)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun <T> Deferred<T>.getCompletedOrNull(): T? =
        if (isCompleted && !isCancelled) getCompleted() else null
}