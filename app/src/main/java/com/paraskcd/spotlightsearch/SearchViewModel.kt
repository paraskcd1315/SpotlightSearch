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

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        updateResults(newQuery)
    }

    private fun updateResults(query: String) {
        viewModelScope.launch {
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

            val localResults = appRepository.searchInstalledApp(query)

            val suggestions = suggestionProvider.fetchSuggestions(query)

            val webSearchItem = SearchResult(
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

            val suggestionItems = suggestions.mapIndexed { index, suggestion ->
                val isCalculator = suggestion.trim().startsWith("=")
                SearchResult(
                    title = if (isCalculator) "$query $suggestion" else suggestion,
                    subtitle = if (isCalculator) "Calculator" else "Suggestion",
                    iconVector = if (isCalculator) Calculate else Icons.Filled.Search,
                    onClick =
                    {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data =
                                "https://www.google.com/search?q=${Uri.encode(suggestion)}".toUri()
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                )
            }

            val contactResults = contactSearchProvider.searchContacts(query)
            val fileResults = fileSearchProvider.searchFiles(query)

            val sortedSuggestions = suggestionItems.sortedByDescending { it.subtitle == "Calculator" }

            val calculatorItem = sortedSuggestions.firstOrNull { it.subtitle == "Calculator" }
            val otherSuggestions = sortedSuggestions.filter { it.subtitle != "Calculator" }

            val playStoreItem = if (localResults.isEmpty()) {
                playStoreSearchProvider.getPlayStoreSearchItem(query)
            } else null

            val orderedResults = buildList {
                calculatorItem?.let {
                    add(SearchResult(title = "Calculator", isHeader = true, onClick = {}))
                    add(it)
                }
                if (contactResults.isNotEmpty()) {
                    add(SearchResult(title = "Contacts", isHeader = true, onClick = {}))
                    addAll(contactResults)
                }
                if (localResults.isNotEmpty()) {
                    add(SearchResult(title = "Apps", isHeader = true, onClick = {}))
                    addAll(localResults)
                }
                if (fileResults.isNotEmpty()) {
                    add(SearchResult(title = "Files", isHeader = true, onClick = {}))
                    addAll(fileResults)
                }
                add(SearchResult(title = "Web", isHeader = true, onClick = {}))
                add(webSearchItem)
                if (localResults.isEmpty() && contactResults.isEmpty() && calculatorItem == null) {
                    if (otherSuggestions.isNotEmpty()) {
                        add(SearchResult(title = "Suggestions", isHeader = true, onClick = {}))
                        addAll(otherSuggestions)
                    }
                }
                playStoreItem?.let {
                    add(SearchResult(title = "Play Store", isHeader = true, onClick = {}))
                    add(it)
                }
            }

            _results.value = permissionPrompt + orderedResults
        }
    }
}