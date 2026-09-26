package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.fadingEdges
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.groupShape
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.AppToggleRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.FilterField
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.BlacklistViewModel

@Composable
fun BlacklistAppsScreen(viewModel: BlacklistViewModel) {
    val apps by viewModel.apps.collectAsState()
    val blacklisted by viewModel.blacklisted.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    val filtered = remember(apps, query) {
        val needle = query.trim()
        apps.orEmpty().filter {
            needle.isEmpty() || it.label.contains(needle, ignoreCase = true) || it.packageName.contains(needle, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SectionTitle(stringResource(R.string.blacklist_section))
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .fadingEdges(listState),
                state = listState
            ) {
                item { Spacer(Modifier.height(SettingsMetrics.FilterFieldTopInset)) }
                if (apps == null) {
                    item { SettingsSkeleton() }
                    return@LazyColumn
                }
                if (filtered.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.settings_no_results),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SettingsMetrics.EmptyTextAlpha),
                            modifier = Modifier.padding(horizontal = SettingsMetrics.FieldHorizontalPadding)
                        )
                    }
                }
                itemsIndexed(filtered, key = { _, app -> app.packageName }) { index, app ->
                    AppToggleRow(
                        packageName = app.packageName,
                        label = app.label,
                        checked = app.packageName in blacklisted,
                        icons = viewModel.icons,
                        shape = groupShape(index, filtered.size),
                        onCheckedChange = { viewModel.setBlacklisted(app.packageName, it) }
                    )
                }
            }
            FilterField(value = query, onValueChange = { query = it })
        }
    }
}
