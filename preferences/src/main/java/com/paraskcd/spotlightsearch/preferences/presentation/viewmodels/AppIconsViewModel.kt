package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.search.domain.model.AppIconChoice
import com.paraskcd.spotlightsearch.search.domain.ports.AppIconPort
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppIconsViewModel @Inject constructor(
    installedApps: InstalledAppsRepository,
    appIconPort: AppIconPort,
    val icons: AppIconLoader
) : ViewModel() {
    val choices: StateFlow<Map<AppKey, AppIconChoice>> = appIconPort.choices()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyMap())

    val apps: StateFlow<List<InstalledApp>?> = combine(installedApps.allApps, choices) { list, chosen ->
        list?.sortedWith(compareBy<InstalledApp> { it.key !in chosen }.thenBy { it.label.lowercase() })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    init {
        installedApps.warmUp()
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
