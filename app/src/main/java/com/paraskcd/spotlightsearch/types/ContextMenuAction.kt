package com.paraskcd.spotlightsearch.types

import androidx.compose.ui.graphics.vector.ImageVector

data class ContextMenuAction(
    val title: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit
)
