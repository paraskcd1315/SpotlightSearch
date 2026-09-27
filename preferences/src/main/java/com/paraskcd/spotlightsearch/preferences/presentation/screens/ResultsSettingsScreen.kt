package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.History
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Rows3
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpGroupedRow
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSectionHeader
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.OptionSheet
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionToggleRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.components.ValueText
import com.paraskcd.spotlightsearch.preferences.presentation.model.ResultsSheet
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ResultsSettingsViewModel
import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.utils.nameRes
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import com.paraskcd.spotlightsearch.search.presentation.utils.titleRes as sectionTitleRes

@Composable
fun ResultsSettingsScreen(viewModel: ResultsSettingsViewModel, onBack: () -> Unit) {
    val config by viewModel.config.collectAsState()
    var order by remember { mutableStateOf(config?.sectionOrder.orEmpty()) }
    var sheet by remember { mutableStateOf(ResultsSheet.NONE) }

    LaunchedEffect(config?.sectionOrder) {
        config?.sectionOrder?.let { order = it }
    }

    val listState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(listState) { from, to ->
        val fromIndex = order.indexOfFirst { it.name == from.key }
        val toIndex = order.indexOfFirst { it.name == to.key }
        if (fromIndex < 0 || toIndex < 0) return@rememberReorderableLazyListState
        order = order.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SpScreenScaffold(
            title = stringResource(R.string.features_results_title),
            backDescription = stringResource(R.string.settings_back),
            onBack = onBack,
            listState = listState
        ) {
            val current = config
            if (current == null) {
                item(key = SKELETON_KEY) { SettingsSkeleton() }
                return@SpScreenScaffold
            }
            item(key = SECTIONS_HEADER_KEY) { SpSectionHeader(stringResource(R.string.results_sections)) }
            item(key = HELP_KEY) {
                Text(
                    text = stringResource(R.string.results_help),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SpTheme.colors.textSecondary,
                    modifier = Modifier.padding(start = SpSpacing.s5, end = SpSpacing.s5, bottom = SpSpacing.s3)
                )
            }
            itemsIndexed(order, key = { _, kind -> kind.name }) { index, kind ->
                ReorderableItem(reorderState, key = kind.name) {
                    Box(modifier = Modifier.longPressDraggableHandle(onDragStopped = { viewModel.reorder(order) })) {
                        SpGroupedRow(index = index, count = order.size) {
                            SectionToggleRow(
                                label = stringResource(kind.sectionTitleRes()),
                                checked = current.shows(kind),
                                onCheckedChange = { viewModel.setVisible(kind, it) }
                            )
                        }
                    }
                }
            }
            item(key = LAYOUT_HEADER_KEY) { SpSectionHeader(stringResource(R.string.results_layout)) }
            item(key = LAYOUT_KEY) {
                SpGroupedList(count = 2) { index ->
                    if (index == 0) {
                        SpSettingsRow(
                            label = stringResource(R.string.results_rows),
                            icon = Lucide.Rows3,
                            onClick = { sheet = ResultsSheet.ROWS },
                            trailing = { ValueText(current.rowsPerSection.toString()) }
                        )
                    } else {
                        SpSettingsRow(
                            label = stringResource(R.string.results_frequent_rows),
                            icon = Lucide.History,
                            onClick = { sheet = ResultsSheet.FREQUENT },
                            trailing = { ValueText(frequentLabel(current.frequentRows)) }
                        )
                    }
                }
            }
            item(key = WEB_HEADER_KEY) { SpSectionHeader(stringResource(R.string.results_web)) }
            item(key = WEB_KEY) {
                SpGroupedList(count = 1) {
                    SpSettingsRow(
                        label = stringResource(R.string.results_engine),
                        icon = Lucide.Globe,
                        onClick = { sheet = ResultsSheet.ENGINE },
                        trailing = { ValueText(stringResource(current.searchEngine.nameRes())) }
                    )
                }
            }
        }

        val current = config ?: return@Box
        OptionSheet(
            visible = sheet == ResultsSheet.ROWS,
            title = stringResource(R.string.results_rows),
            options = (SearchLimits.MIN_ROWS_PER_SECTION..SearchLimits.MAX_ROWS_PER_SECTION).toList(),
            selected = current.rowsPerSection,
            label = { it.toString() },
            onSelect = {
                sheet = ResultsSheet.NONE
                viewModel.setRows(it)
            },
            onDismiss = { sheet = ResultsSheet.NONE }
        )
        OptionSheet(
            visible = sheet == ResultsSheet.FREQUENT,
            title = stringResource(R.string.results_frequent_rows),
            options = (0..SearchLimits.MAX_FREQUENT_ROWS).toList(),
            selected = current.frequentRows,
            label = { frequentLabel(it) },
            onSelect = {
                sheet = ResultsSheet.NONE
                viewModel.setFrequentRows(it)
            },
            onDismiss = { sheet = ResultsSheet.NONE }
        )
        OptionSheet(
            visible = sheet == ResultsSheet.ENGINE,
            title = stringResource(R.string.results_engine),
            options = WebSearchEngine.entries,
            selected = current.searchEngine,
            label = { stringResource(it.nameRes()) },
            onSelect = {
                sheet = ResultsSheet.NONE
                viewModel.setEngine(it)
            },
            onDismiss = { sheet = ResultsSheet.NONE }
        )
    }
}

@Composable
private fun frequentLabel(rows: Int): String =
    if (rows == 0) stringResource(R.string.results_off) else rows.toString()

private const val SKELETON_KEY = "skeleton"
private const val SECTIONS_HEADER_KEY = "sections_header"
private const val HELP_KEY = "help"
private const val LAYOUT_HEADER_KEY = "layout_header"
private const val LAYOUT_KEY = "layout"
private const val WEB_HEADER_KEY = "web_header"
private const val WEB_KEY = "web"
