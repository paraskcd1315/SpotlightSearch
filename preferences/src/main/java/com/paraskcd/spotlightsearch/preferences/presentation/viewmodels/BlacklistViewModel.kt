package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.domain.repository.SearchSettingsRepository
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlacklistViewModel @Inject constructor(
    private val settings: SearchSettingsRepository,
    installedApps: InstalledAppsRepository,
    val icons: AppIconLoader
) : ViewModel() {
    val apps: StateFlow<List<InstalledApp>?> = installedApps.allApps
        .map { list -> list?.sortedBy { it.label.lowercase() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    val blacklisted: StateFlow<Set<String>> = settings.blacklist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptySet())

    init {
        installedApps.warmUp()
    }

    fun setBlacklisted(packageName: String, blacklisted: Boolean) =
        viewModelScope.launch { settings.setBlacklisted(packageName, blacklisted) }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
