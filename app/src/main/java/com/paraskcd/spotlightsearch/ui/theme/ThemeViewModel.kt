package com.paraskcd.spotlightsearch.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.data.entities.UserThemeEntity
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.enums.ColorOverrideKey
import com.paraskcd.spotlightsearch.enums.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.toLong

data class ThemeUi(
    val mode: ThemeMode,
    val enableBlur: Boolean,
    val surface: Color? = null,
    val surfaceBright: Color? = null,
    val background: Color? = null,
    val surfaceTint: Color? = null,
    val onSurface: Color? = null,
    val outline: Color? = null
)

@HiltViewModel
class ThemeViewModel @Inject constructor(
    repo: UserThemeRepository
) : ViewModel() {
    val state: StateFlow<ThemeUi> = repo.observe()
        .map { it.toUi() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ThemeUi(mode = ThemeMode.AUTO, enableBlur = false)
        )

    private fun UserThemeEntity?.toUi(): ThemeUi = ThemeUi(
        mode = this?.theme ?: ThemeMode.AUTO,
        enableBlur = this?.enableBlur ?: false,
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
                // Crea fila sin sobreescribir nada (queda enableBlur = null)
                repo.merge()
            }
        }
    }
}

@Composable
fun buildColorScheme(
    viewModel: ThemeViewModel,
    dynamic: Boolean
): ColorScheme {
    val ui = viewModel.state.collectAsState().value
    val systemDark = isSystemInDarkTheme()
    val effectiveDark = when (ui.mode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.AUTO -> systemDark
    }

    val context = LocalContext.current
    val baseScheme: ColorScheme = when {
        dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (effectiveDark) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        effectiveDark -> darkColorScheme()
        else -> lightColorScheme()
    }

    return baseScheme.copy(
        surface = ui.surface ?: baseScheme.surface,
        surfaceBright = ui.surfaceBright ?: baseScheme.surfaceBright,
        background = ui.background ?: baseScheme.background,
        surfaceTint = ui.surfaceTint ?: baseScheme.surfaceTint,
        onSurface = ui.onSurface ?: baseScheme.onSurface,
        outline = ui.outline ?: baseScheme.outline,
    )
}