package com.paraskcd.spotlightsearch.ui.pages.settings.features.quicksearch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.SettingsViewModel
import com.paraskcd.spotlightsearch.icons.Bars
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon
import org.burnoutcrew.reorderable.*

@Composable
fun QuicksearchPage(
    navController: NavController,
    vm: SettingsViewModel
) {
    val dbList by vm.quickSearchProviders.collectAsState()
    var uiList by remember { mutableStateOf(dbList) }

    LaunchedEffect(dbList) {
        if (dbList.map { it.packageName } != uiList.map { it.packageName }) {
            uiList = dbList
        }
    }

    if (uiList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(Modifier.padding(32.dp))
        }
        return
    }

    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            uiList = uiList.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        },
        onDragEnd = { _, _ ->
            vm.reorder(uiList)
        }
    )

    Column {
        Text(
            "Tap, hold and Drag to reorder, toggle the switch to disable (If you see an app without icon it means it is not installed, and will not show up in the search)",
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        LazyColumn(
            state = reorderState.listState,
            modifier = Modifier
                .reorderable(reorderState)
                .detectReorderAfterLongPress(reorderState)
        ) {
            itemsIndexed(uiList, key = { _, item -> item.packageName }) { index, item ->
                val shape = when {
                    uiList.size == 1 -> RoundedCornerShape(24.dp)
                    index == 0 -> RoundedCornerShape(
                        topStart = 24.dp, topEnd = 24.dp,
                        bottomStart = 8.dp, bottomEnd = 8.dp
                    )
                    index == uiList.size - 1 -> RoundedCornerShape(
                        topStart = 8.dp, topEnd = 8.dp,
                        bottomStart = 24.dp, bottomEnd = 24.dp
                    )
                    else -> RoundedCornerShape(8.dp)
                }

                ReorderableItemCustom(reorderState, key = item.packageName) { isDragging ->
                    BaseRowContainer(shape = shape) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Bars,
                                contentDescription = "Drag",
                            )
                            RowWithIcon(
                                iconDrawable = item.icon,
                                text = item.label,
                                subtext = item.packageName
                            )
                        }
                        Switch(
                            checked = item.enabled,
                            onCheckedChange = { vm.toggle(item.packageName, it) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ReorderableItemCustom(
    reorderableState: ReorderableState<*>,
    key: Any?,
    modifier: Modifier = Modifier,
    index: Int? = null,
    orientationLocked: Boolean = true,
    content: @Composable BoxScope.(isDragging: Boolean) -> Unit,
) = ReorderableItem(reorderableState, key, modifier, Modifier.animateItem(), orientationLocked, index, content)