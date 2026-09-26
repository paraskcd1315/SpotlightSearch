package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.LayoutGrid
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.SearchSourcesViewModel

@Composable
fun ManageAppsScreen(viewModel: SearchSourcesViewModel, onNavigate: (String) -> Unit, onBack: () -> Unit) {
    val config by viewModel.config.collectAsState()
    SpScreenScaffold(
        title = stringResource(R.string.apps_section),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack
    ) {
        item {
            val current = config
            if (current == null) {
                SettingsSkeleton(rows = 2)
                return@item
            }
            SpGroupedList(count = 2) { index ->
                if (index == 0) {
                    SwitchRow(
                        text = stringResource(R.string.apps_enable),
                        icon = Lucide.LayoutGrid,
                        checked = current.appsEnabled,
                        onCheckedChange = viewModel::setApps
                    )
                } else {
                    SpSettingsRow(
                        label = stringResource(R.string.apps_blacklist),
                        icon = Lucide.EyeOff,
                        onClick = { onNavigate(SettingsRoute.APPS_BLACKLIST) }
                    )
                }
            }
        }
    }
}
