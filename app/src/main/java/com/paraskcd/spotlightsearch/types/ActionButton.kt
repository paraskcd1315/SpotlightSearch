package com.paraskcd.spotlightsearch.types

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector

data class ActionButton(
    val label: String,
    val icon: Drawable? = null,
    val iconVector: ImageVector? = null,
    val onClick: () -> Unit,
)