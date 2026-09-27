package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpSearch
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpGroupedRow
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn
import com.paraskcd.spotlightsearch.preferences.presentation.components.AppIconRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.AppIconsViewModel

@Composable
fun AppIconsScreen(viewModel: AppIconsViewModel, onNavigate: (String) -> Unit, onBack: () -> Unit) {
    val apps by viewModel.apps.collectAsState()
    val choices by viewModel.choices.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(apps, query) {
        val needle = query.trim()
        apps.orEmpty().filter {
            needle.isEmpty() || it.label.contains(needle, ignoreCase = true) || it.packageName.contains(needle, ignoreCase = true)
        }
    }

    SpScreenScaffold(
        title = stringResource(R.string.appearance_app_icons),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack,
        attachment = {
            SpSearch(
                value = query,
                onValueChange = { query = it },
                placeholder = stringResource(R.string.blacklist_filter),
                clearDescription = stringResource(R.string.blacklist_filter_clear)
            )
        }
    ) {
        if (apps == null) {
            item { SettingsSkeleton() }
            return@SpScreenScaffold
        }
        if (filtered.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.settings_no_results),
                    style = MaterialTheme.typography.bodyLarge,
                    color = SpTheme.colors.textTertiary,
                    modifier = Modifier.padding(horizontal = SpSpacing.s5)
                )
            }
        }
        itemsIndexed(filtered, key = { _, app -> app.key.toString() }) { index, app ->
            SpGroupedRow(index = index, count = filtered.size) {
                AppIconRow(
                    app = app,
                    custom = app.key in choices,
                    icons = viewModel.icons,
                    onClick = { onNavigate(SettingsRoute.appIcon(app.packageName, ProfileColumn.of(app.profile))) }
                )
            }
        }
    }
}
