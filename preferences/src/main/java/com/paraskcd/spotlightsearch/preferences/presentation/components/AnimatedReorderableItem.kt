package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableState

@Composable
fun LazyItemScope.AnimatedReorderableItem(
    state: ReorderableState<*>,
    key: Any?,
    content: @Composable BoxScope.(isDragging: Boolean) -> Unit
) = ReorderableItem(state, key, Modifier, Modifier.animateItem(), true, null, content)
