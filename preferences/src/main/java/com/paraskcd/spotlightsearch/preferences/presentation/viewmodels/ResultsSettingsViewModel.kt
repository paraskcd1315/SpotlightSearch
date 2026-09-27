package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.domain.repository.SearchSettingsRepository
import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsSettingsViewModel @Inject constructor(
    private val settings: SearchSettingsRepository
) : ViewModel() {
    val config: StateFlow<SearchConfig?> = settings.config()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    fun reorder(order: List<SectionKind>) = viewModelScope.launch { settings.setSectionOrder(order) }

    fun setVisible(kind: SectionKind, visible: Boolean) = viewModelScope.launch { settings.setSectionVisible(kind, visible) }

    fun setRows(rows: Int) = viewModelScope.launch { settings.setRowsPerSection(rows) }

    fun setFrequentRows(rows: Int) = viewModelScope.launch { settings.setFrequentRows(rows) }

    fun setEngine(engine: WebSearchEngine) = viewModelScope.launch { settings.setSearchEngine(engine) }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
