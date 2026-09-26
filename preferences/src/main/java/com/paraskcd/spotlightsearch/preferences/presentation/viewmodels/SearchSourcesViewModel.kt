package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.domain.repository.SearchSettingsRepository
import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSourcesViewModel @Inject constructor(
    private val settings: SearchSettingsRepository
) : ViewModel() {
    val config: StateFlow<SearchConfig?> = settings.config()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    fun setApps(enabled: Boolean) = viewModelScope.launch { settings.setAppsEnabled(enabled) }

    fun setContacts(enabled: Boolean) = viewModelScope.launch { settings.setContactsEnabled(enabled) }

    fun setWebSuggestions(enabled: Boolean) = viewModelScope.launch { settings.setWebSuggestionsEnabled(enabled) }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
