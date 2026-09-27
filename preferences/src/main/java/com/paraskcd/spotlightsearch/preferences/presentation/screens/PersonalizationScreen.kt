package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.Droplets
import com.composables.icons.lucide.LayoutGrid
import com.composables.icons.lucide.Layers
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Paintbrush
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.SunMoon
import com.composables.icons.lucide.Type
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSectionHeader
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.domain.model.GlassStrength
import com.paraskcd.spotlightsearch.preferences.domain.model.TextSize
import com.paraskcd.spotlightsearch.preferences.presentation.components.ColorSwatch
import com.paraskcd.spotlightsearch.preferences.presentation.components.ConfirmDialog
import com.paraskcd.spotlightsearch.preferences.presentation.components.OptionSheet
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.ThemeModeDialog
import com.paraskcd.spotlightsearch.preferences.presentation.components.ValueText
import com.paraskcd.spotlightsearch.preferences.presentation.model.AppearanceSheet
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.utils.labelRes
import com.paraskcd.spotlightsearch.preferences.presentation.utils.swatchFallback
import com.paraskcd.spotlightsearch.preferences.presentation.utils.titleRes
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.domain.model.AppResultsLayout
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur

@Composable
fun PersonalizationScreen(viewModel: ThemeViewModel, onNavigate: (String) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val supportsBlur = remember { WindowBlur.isAvailable(context, userEnabled = true) }
    var sheet by remember { mutableStateOf(AppearanceSheet.NONE) }
    val keys = ColorOverrideKey.entries
    val scheme = MaterialTheme.colorScheme
    val appearanceRows = buildList<@Composable () -> Unit> {
        add {
            SpSettingsRow(
                label = stringResource(R.string.appearance_select_theme),
                icon = Lucide.SunMoon,
                onClick = { sheet = AppearanceSheet.THEME },
                trailing = { ValueText(stringResource(state.mode.labelRes())) }
            )
        }
        add {
            SwitchRow(
                text = stringResource(R.string.appearance_show_branding),
                icon = Lucide.BadgeCheck,
                checked = state.showBranding,
                onCheckedChange = viewModel::setBranding
            )
        }
        if (supportsBlur) add {
            SwitchRow(
                text = stringResource(R.string.appearance_enable_blur),
                icon = Lucide.Droplets,
                checked = state.enableBlur,
                onCheckedChange = viewModel::setBlur
            )
        }
        add {
            SpSettingsRow(
                label = stringResource(R.string.appearance_glass),
                icon = Lucide.Layers,
                onClick = { sheet = AppearanceSheet.GLASS },
                trailing = { ValueText(stringResource(state.glassStrength.labelRes())) }
            )
        }
        add {
            SpSettingsRow(
                label = stringResource(R.string.appearance_text_size),
                icon = Lucide.Type,
                onClick = { sheet = AppearanceSheet.TEXT },
                trailing = { ValueText(stringResource(state.textSize.labelRes())) }
            )
        }
        add {
            SpSettingsRow(
                label = stringResource(R.string.appearance_app_layout),
                icon = Lucide.LayoutGrid,
                onClick = { sheet = AppearanceSheet.LAYOUT },
                trailing = { ValueText(stringResource(state.appLayout.labelRes())) }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SpScreenScaffold(
            title = stringResource(R.string.appearance_header),
            backDescription = stringResource(R.string.settings_back),
            onBack = onBack
        ) {
            item { SpSectionHeader(stringResource(R.string.appearance_section)) }
            item { SpGroupedList(count = appearanceRows.size) { index -> appearanceRows[index]() } }
            item { SpSectionHeader(stringResource(R.string.personalization_section)) }
            item {
                SpGroupedList(count = keys.size) { index ->
                    val key = keys[index]
                    val color = state.colors[key] ?: key.swatchFallback(scheme)
                    SpSettingsRow(
                        label = stringResource(key.titleRes()),
                        icon = Lucide.Paintbrush,
                        onClick = { onNavigate(SettingsRoute.colorPicker(key.name)) },
                        trailing = { ColorSwatch(color) }
                    )
                }
            }
            item { Spacer(Modifier.height(SpMetrics.sectionGap)) }
            item {
                SpGroupedList(count = 1) {
                    SpSettingsRow(
                        label = stringResource(R.string.personalization_reset_colors),
                        icon = Lucide.RotateCcw,
                        onClick = { sheet = AppearanceSheet.RESET }
                    )
                }
            }
        }
        ThemeModeDialog(
            visible = sheet == AppearanceSheet.THEME,
            selected = state.mode,
            onSelect = {
                sheet = AppearanceSheet.NONE
                viewModel.setMode(it)
            },
            onDismiss = { sheet = AppearanceSheet.NONE }
        )
        OptionSheet(
            visible = sheet == AppearanceSheet.GLASS,
            title = stringResource(R.string.appearance_glass),
            options = GlassStrength.entries,
            selected = state.glassStrength,
            label = { stringResource(it.labelRes()) },
            onSelect = {
                sheet = AppearanceSheet.NONE
                viewModel.setGlassStrength(it)
            },
            onDismiss = { sheet = AppearanceSheet.NONE }
        )
        OptionSheet(
            visible = sheet == AppearanceSheet.TEXT,
            title = stringResource(R.string.appearance_text_size),
            options = TextSize.entries,
            selected = state.textSize,
            label = { stringResource(it.labelRes()) },
            onSelect = {
                sheet = AppearanceSheet.NONE
                viewModel.setTextSize(it)
            },
            onDismiss = { sheet = AppearanceSheet.NONE }
        )
        OptionSheet(
            visible = sheet == AppearanceSheet.LAYOUT,
            title = stringResource(R.string.appearance_app_layout),
            options = AppResultsLayout.entries,
            selected = state.appLayout,
            label = { stringResource(it.labelRes()) },
            onSelect = {
                sheet = AppearanceSheet.NONE
                viewModel.setAppLayout(it)
            },
            onDismiss = { sheet = AppearanceSheet.NONE }
        )
        ConfirmDialog(
            visible = sheet == AppearanceSheet.RESET,
            title = stringResource(R.string.reset_colors_title),
            message = stringResource(R.string.reset_colors_message),
            confirmLabel = stringResource(R.string.reset_colors_confirm),
            onConfirm = {
                sheet = AppearanceSheet.NONE
                viewModel.clearColors()
            },
            onDismiss = { sheet = AppearanceSheet.NONE }
        )
    }
}
