package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.GroupSurface
import com.paraskcd.spotlightsearch.designsystem.icons.Apps
import com.paraskcd.spotlightsearch.designsystem.icons.ChevronRight
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.NavigationRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.SearchSourcesViewModel

@Composable
fun ManageAppsScreen(viewModel: SearchSourcesViewModel, onNavigate: (String) -> Unit) {
    val config by viewModel.config.collectAsState()
    LazyColumn {
        item { SectionTitle(stringResource(R.string.apps_section)) }
        item {
            val current = config
            if (current == null) {
                SettingsSkeleton()
                return@item
            }
            GroupSurface(count = 2) { index, shape ->
                if (index == 0) {
                    SwitchRow(stringResource(R.string.apps_enable), Apps, current.appsEnabled, shape, viewModel::setApps)
                } else {
                    NavigationRow(
                        title = stringResource(R.string.apps_blacklist),
                        subtitle = null,
                        icon = Apps,
                        trailingIcon = ChevronRight,
                        shape = shape,
                        onClick = { onNavigate(SettingsRoute.APPS_BLACKLIST) }
                    )
                }
            }
        }
    }
}
