package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Paintbrush
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.SunMoon
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSectionHeader
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.presentation.components.ColorSwatch
import com.paraskcd.spotlightsearch.preferences.presentation.components.ConfirmDialog
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.ThemeModeDialog
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.utils.labelRes
import com.paraskcd.spotlightsearch.preferences.presentation.utils.swatchFallback
import com.paraskcd.spotlightsearch.preferences.presentation.utils.titleRes
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur

@Composable
fun PersonalizationScreen(viewModel: ThemeViewModel, onNavigate: (String) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val supportsBlur = remember { WindowBlur.isAvailable(context, userEnabled = true) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    val keys = ColorOverrideKey.entries
    val scheme = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()) {
        SpScreenScaffold(
            title = stringResource(R.string.appearance_header),
            backDescription = stringResource(R.string.settings_back),
            onBack = onBack
        ) {
            item { SpSectionHeader(stringResource(R.string.appearance_section)) }
            item {
                SpGroupedList(count = if (supportsBlur) 3 else 2) { index ->
                    when {
                        index == 0 -> SpSettingsRow(
                            label = stringResource(R.string.appearance_select_theme),
                            icon = Lucide.SunMoon,
                            onClick = { showThemeDialog = true },
                            trailing = {
                                Text(
                                    stringResource(state.mode.labelRes()),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SpTheme.colors.textSecondary
                                )
                            }
                        )
                        index == 1 -> SwitchRow(
                            text = stringResource(R.string.appearance_show_branding),
                            icon = Lucide.BadgeCheck,
                            checked = state.showBranding,
                            onCheckedChange = viewModel::setBranding
                        )
                        else -> SwitchRow(
                            text = stringResource(R.string.appearance_enable_blur),
                            icon = Lucide.Droplets,
                            checked = state.enableBlur,
                            onCheckedChange = viewModel::setBlur
                        )
                    }
                }
            }
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
                        onClick = { showResetDialog = true }
                    )
                }
            }
        }
        ThemeModeDialog(
            visible = showThemeDialog,
            selected = state.mode,
            onSelect = {
                showThemeDialog = false
                viewModel.setMode(it)
            },
            onDismiss = { showThemeDialog = false }
        )
        ConfirmDialog(
            visible = showResetDialog,
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
