package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.GroupSurface
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.HeaderCard
import com.paraskcd.spotlightsearch.designsystem.icons.Blur
import com.paraskcd.spotlightsearch.designsystem.icons.Palette
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.presentation.components.ColorSwatch
import com.paraskcd.spotlightsearch.preferences.presentation.components.ConfirmDialog
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.ThemeModeDialog
import com.paraskcd.spotlightsearch.preferences.presentation.components.ValueRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.ValueText
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.utils.labelRes
import com.paraskcd.spotlightsearch.preferences.presentation.utils.swatchFallback
import com.paraskcd.spotlightsearch.preferences.presentation.utils.titleRes
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur

@Composable
fun PersonalizationScreen(viewModel: ThemeViewModel, onNavigate: (String) -> Unit) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val supportsBlur = remember { WindowBlur.isAvailable(context, userEnabled = true) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    val keys = ColorOverrideKey.entries

    LazyColumn {
        item { HeaderCard(stringResource(R.string.appearance_header), icon = Palette) }
        item { SectionTitle(stringResource(R.string.appearance_section)) }
        item {
            GroupSurface(count = if (supportsBlur) 2 else 1) { index, shape ->
                if (index == 0) {
                    ValueRow(stringResource(R.string.appearance_select_theme), Palette, shape, { showThemeDialog = true }) {
                        ValueText(stringResource(state.mode.labelRes()))
                    }
                } else {
                    SwitchRow(stringResource(R.string.appearance_enable_blur), Blur, state.enableBlur, shape, viewModel::setBlur)
                }
            }
        }
        item { SectionTitle(stringResource(R.string.personalization_section)) }
        item {
            GroupSurface(count = keys.size + 1) { index, shape ->
                if (index == 0) {
                    ValueRow(stringResource(R.string.personalization_reset_colors), Palette, shape, { showResetDialog = true }) {}
                } else {
                    val key = keys[index - 1]
                    val color = state.colors[key] ?: key.swatchFallback(MaterialTheme.colorScheme)
                    ValueRow(stringResource(key.titleRes()), Palette, shape, { onNavigate(SettingsRoute.colorPicker(key.name)) }) {
                        ColorSwatch(color)
                    }
                }
            }
        }
        item { Spacer(Modifier.height(SettingsMetrics.BottomSpacer)) }
    }

    if (showThemeDialog) {
        ThemeModeDialog(
            selected = state.mode,
            onSelect = {
                showThemeDialog = false
                viewModel.setMode(it)
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showResetDialog) {
        ConfirmDialog(
            title = stringResource(R.string.reset_colors_title),
            message = stringResource(R.string.reset_colors_message),
            confirmLabel = stringResource(R.string.reset_colors_confirm),
            onConfirm = {
                showResetDialog = false
                viewModel.clearColors()
            },
            onDismiss = { showResetDialog = false }
        )
    }
}
