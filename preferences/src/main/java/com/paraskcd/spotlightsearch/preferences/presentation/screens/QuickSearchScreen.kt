package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Box
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
import com.composables.icons.lucide.GripVertical
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpGroupedRow
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.AppToggleRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.QuickSearchViewModel
import com.paraskcd.spotlightsearch.search.presentation.utils.nameRes
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun QuickSearchScreen(viewModel: QuickSearchViewModel, onBack: () -> Unit) {
    val items by viewModel.items.collectAsState()
    var uiList by remember { mutableStateOf(items.orEmpty()) }

    LaunchedEffect(items) {
        items?.let { uiList = it }
    }

    val listState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(listState) { from, to ->
        val fromIndex = uiList.indexOfFirst { it.packageName == from.key }
        val toIndex = uiList.indexOfFirst { it.packageName == to.key }
        if (fromIndex < 0 || toIndex < 0) return@rememberReorderableLazyListState
        uiList = uiList.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
    }

    SpScreenScaffold(
        title = stringResource(R.string.features_quick_search_title),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack,
        listState = listState
    ) {
        item(key = HEADER_KEY) {
            Text(
                text = stringResource(R.string.quick_search_help),
                style = MaterialTheme.typography.bodyMedium,
                color = SpTheme.colors.textSecondary,
                modifier = Modifier.padding(start = SpSpacing.s5, end = SpSpacing.s5, bottom = SpSpacing.s4)
            )
        }
        if (items == null) {
            item(key = SKELETON_KEY) { SettingsSkeleton() }
            return@SpScreenScaffold
        }
        itemsIndexed(uiList, key = { _, item -> item.packageName }) { index, item ->
            ReorderableItem(reorderState, key = item.packageName) {
                val label = QuickSearchService.fromPackage(item.packageName)
                    ?.let { stringResource(it.nameRes()) }
                    ?: item.packageName
                Box(
                    modifier = Modifier.longPressDraggableHandle(
                        onDragStopped = { viewModel.reorder(uiList.map { it.packageName }) }
                    )
                ) {
                    SpGroupedRow(index = index, count = uiList.size) {
                        AppToggleRow(
                            packageName = item.packageName,
                            label = label,
                            checked = item.enabled,
                            icons = viewModel.icons,
                            onCheckedChange = { viewModel.toggle(item.packageName, it) },
                            dragIcon = Lucide.GripVertical
                        )
                    }
                }
            }
        }
    }
}

private const val HEADER_KEY = 0
private const val SKELETON_KEY = 1
