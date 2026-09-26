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
import com.paraskcd.spotlightsearch.preferences.presentation.components.AppToggleRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.BlacklistViewModel

@Composable
fun BlacklistAppsScreen(viewModel: BlacklistViewModel, onBack: () -> Unit) {
    val apps by viewModel.apps.collectAsState()
    val blacklisted by viewModel.blacklisted.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(apps, query) {
        val needle = query.trim()
        apps.orEmpty().filter {
            needle.isEmpty() || it.label.contains(needle, ignoreCase = true) || it.packageName.contains(needle, ignoreCase = true)
        }
    }
    val placeholder = stringResource(R.string.blacklist_filter)
    val clear = stringResource(R.string.blacklist_filter_clear)

    SpScreenScaffold(
        title = stringResource(R.string.blacklist_section),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack,
        attachment = {
            SpSearch(value = query, onValueChange = { query = it }, placeholder = placeholder, clearDescription = clear)
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
        itemsIndexed(filtered, key = { _, app -> app.packageName }) { index, app ->
            SpGroupedRow(index = index, count = filtered.size) {
                AppToggleRow(
                    packageName = app.packageName,
                    label = app.label,
                    checked = app.packageName in blacklisted,
                    icons = viewModel.icons,
                    onCheckedChange = { viewModel.setBlacklisted(app.packageName, it) }
                )
            }
        }
    }
}
