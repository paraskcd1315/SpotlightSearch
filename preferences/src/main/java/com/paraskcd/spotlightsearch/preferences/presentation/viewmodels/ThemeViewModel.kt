package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import com.paraskcd.spotlightsearch.preferences.domain.repository.ThemeRepository
import com.paraskcd.spotlightsearch.preferences.presentation.model.ThemeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repository: ThemeRepository
) : ViewModel() {
    val state: StateFlow<ThemeUi> = repository.settings()
        .map { settings ->
            ThemeUi(
                mode = settings.mode,
                enableBlur = settings.blurEnabled,
                colors = settings.colors.mapValues { Color(it.value) }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), ThemeUi())

    fun setMode(mode: ThemeMode) = viewModelScope.launch { repository.setMode(mode) }

    fun setBlur(enabled: Boolean) = viewModelScope.launch { repository.setBlur(enabled) }

    fun setColor(key: ColorOverrideKey, argb: Int) = viewModelScope.launch { repository.setColor(key, argb) }

    fun clearColor(key: ColorOverrideKey) = viewModelScope.launch { repository.clearColor(key) }

    fun clearColors() = viewModelScope.launch { repository.clearColors() }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
