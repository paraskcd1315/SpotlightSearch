package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.domain.repository.SearchSettingsRepository
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickSearchViewModel @Inject constructor(
    private val settings: SearchSettingsRepository,
    val icons: AppIconLoader
) : ViewModel() {
    val items: StateFlow<List<QuickSearchPreference>?> = settings.quickSearch()
        .map { list -> list.sortedBy { it.sortOrder } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    init {
        viewModelScope.launch { settings.ensureQuickSearchDefaults() }
    }

    fun toggle(packageName: String, enabled: Boolean) =
        viewModelScope.launch { settings.setQuickSearchEnabled(packageName, enabled) }

    fun reorder(packagesInOrder: List<String>) =
        viewModelScope.launch { settings.reorderQuickSearch(packagesInOrder) }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
