package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.groupShape
import com.paraskcd.spotlightsearch.designsystem.icons.Bars
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.AppToggleRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.QuickSearchViewModel
import com.paraskcd.spotlightsearch.search.presentation.utils.nameRes
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun QuickSearchScreen(viewModel: QuickSearchViewModel) {
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

    LazyColumn(state = listState) {
        item(key = HEADER_KEY) { SectionTitle(stringResource(R.string.quick_search_help)) }
        if (items == null) {
            item(key = SKELETON_KEY) { SettingsSkeleton() }
            return@LazyColumn
        }
        itemsIndexed(uiList, key = { _, item -> item.packageName }) { index, item ->
            ReorderableItem(reorderState, key = item.packageName) { isDragging ->
                val label = QuickSearchService.fromPackage(item.packageName)
                    ?.let { stringResource(it.nameRes()) }
                    ?: item.packageName
                Box(
                    modifier = Modifier.longPressDraggableHandle(
                        onDragStopped = { viewModel.reorder(uiList.map { it.packageName }) }
                    )
                ) {
                    AppToggleRow(
                        packageName = item.packageName,
                        label = label,
                        checked = item.enabled,
                        icons = viewModel.icons,
                        shape = groupShape(index, uiList.size),
                        onCheckedChange = { viewModel.toggle(item.packageName, it) },
                        isDragging = isDragging,
                        dragIcon = Bars
                    )
                }
            }
        }
    }
}

private const val HEADER_KEY = 0
private const val SKELETON_KEY = 1
