package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.paraskcd.spotlightsearch.preferences.presentation.components.AnimatedReorderableItem
import com.paraskcd.spotlightsearch.preferences.presentation.components.AppToggleRow
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.QuickSearchViewModel
import com.paraskcd.spotlightsearch.search.presentation.utils.nameRes
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun QuickSearchScreen(viewModel: QuickSearchViewModel) {
    val items by viewModel.items.collectAsState()
    var uiList by remember { mutableStateOf(items.orEmpty()) }

    LaunchedEffect(items) {
        items?.let { uiList = it }
    }

    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            uiList = uiList.toMutableList().apply { add(to.index - HEADER_ITEMS, removeAt(from.index - HEADER_ITEMS)) }
        },
        canDragOver = { draggedOver, _ -> draggedOver.key is String },
        onDragEnd = { _, _ -> viewModel.reorder(uiList.map { it.packageName }) }
    )

    LazyColumn(
        state = reorderState.listState,
        modifier = Modifier
            .reorderable(reorderState)
            .detectReorderAfterLongPress(reorderState)
    ) {
        item(key = HEADER_KEY) { SectionTitle(stringResource(R.string.quick_search_help)) }
        if (items == null) {
            item(key = SKELETON_KEY) { SettingsSkeleton() }
            return@LazyColumn
        }
        itemsIndexed(uiList, key = { _, item -> item.packageName }) { index, item ->
            AnimatedReorderableItem(reorderState, key = item.packageName) { isDragging ->
                val label = QuickSearchService.fromPackage(item.packageName)
                    ?.let { stringResource(it.nameRes()) }
                    ?: item.packageName
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

private const val HEADER_ITEMS = 1
private const val HEADER_KEY = 0
private const val SKELETON_KEY = 1
