package com.paraskcd.spotlightsearch.types

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingPageItem(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val route: String
)
