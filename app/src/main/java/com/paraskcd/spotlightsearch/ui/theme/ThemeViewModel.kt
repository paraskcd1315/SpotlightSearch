package com.paraskcd.spotlightsearch.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.UserThemeEntity
import com.paraskcd.spotlightsearch.preferences.data.UserThemeRepository
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    repo: UserThemeRepository
) : ViewModel() {
    val state: StateFlow<ThemeUi> = repo.observe()
        .map { it.toUi() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ThemeUi(mode = ThemeMode.AUTO, enableBlur = true)
        )

    private fun UserThemeEntity?.toUi(): ThemeUi = ThemeUi(
        mode = this?.theme ?: ThemeMode.AUTO,
        enableBlur = this?.enableBlur ?: true,
        surface = this?.surfaceColor?.let { Color(it) },
        surfaceBright = this?.surfaceBrightColor?.let { Color(it) },
        background = this?.backgroundColor?.let { Color(it) },
        surfaceTint = this?.surfaceTintColor?.let { Color(it) },
        onSurface = this?.onSurfaceColor?.let { Color(it) },
        outline = this?.outlineColor?.let { Color(it) }
    )

    init {
        viewModelScope.launch {
            if (repo.get() == null) {
                repo.merge()
            }
        }
    }
}
