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
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import android.os.Environment
import android.provider.Settings
import androidx.compose.material.icons.filled.Warning
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
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
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private var searchJob: Job? = null

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        updateResults(newQuery)
    }

    fun onSearch() {
        val firstResult = _results.value.firstOrNull { !it.isHeader } ?: return
        firstResult.onClick()
    }

    private fun updateResults(query: String) {
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
                    add(
                        SearchResult(
                            title = "Allow file access",
                            subtitle = "Required to search local files",
                            iconVector = Icons.Filled.Warning,
                            onClick = {
                                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                                    data = "package:${context.packageName}".toUri()
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            }
                        )
                    )
                }
                if (needsContactPermission) {
                    add(
                        SearchResult(
                            title = "Allow contact access",
                            subtitle = "Required to search contacts",
                            iconVector = Icons.Filled.Warning,
                            onClick = {
                                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            }
                        )
                    )
                }
            }

            _results.value = permissionPrompt

            val results = mutableListOf<SearchResult>()

            val appDeferred = async { appRepository.searchInstalledApp(query) }
            val contactDeferred = async { contactSearchProvider.searchContacts(query) }
            val fileDeferred = async { fileSearchProvider.searchFiles(query) }
            val suggestionDeferred = async { suggestionProvider.fetchSuggestions(query) }
            val playStoreDeferred = async { playStoreSearchProvider.getPlayStoreSearchItem(query) }

            appDeferred.await().takeIf { it.isNotEmpty() }?.let {
                results.add(SearchResult(title = "Apps", isHeader = true, onClick = {}))
                results.addAll(it)
                _results.value += results
                results.clear()
            }

            contactDeferred.await().takeIf { it.isNotEmpty() }?.let {
                results.add(SearchResult(title = "Contacts", isHeader = true, onClick = {}))
                results.addAll(it)
                _results.value += results
                results.clear()
            }

            fileDeferred.await().takeIf { it.isNotEmpty() }?.let {
                results.add(SearchResult(title = "Files", isHeader = true, onClick = {}))
                results.addAll(it)
                _results.value += results
                results.clear()
            }

            _results.value += SearchResult(
                title = "Search \"$query\" on the web",
                subtitle = "Web Search",
                iconVector = Icons.Filled.Search,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = "https://www.google.com/search?q=${Uri.encode(query)}".toUri()
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                }
            )

            playStoreDeferred.await()?.let {
                results.add(SearchResult(title = "Play Store", isHeader = true, onClick = {}))
                results.add(it)
                _results.value += results
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
                        }
                    )
                }

                val calculatorItem = sorted.firstOrNull { it.subtitle == "Calculator" }
                val otherSuggestions = sorted.filter { it.subtitle != "Calculator" }

                calculatorItem?.let {
                    results.add(SearchResult(title = "Calculator", isHeader = true, onClick = {}))
                    results.add(it)
                    _results.value += results
                    results.clear()
                }

                if (appDeferred.await().isEmpty() && contactDeferred.await().isEmpty() && calculatorItem == null) {
                    if (otherSuggestions.isNotEmpty()) {
                        results.add(SearchResult(title = "Suggestions", isHeader = true, onClick = {}))
                        results.addAll(otherSuggestions)
                        _results.value += results
                        results.clear()
                    }
                }
            }
        }
    }
}