package com.paraskcd.spotlightsearch.preferences.presentation.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingPageItem(
    @param:StringRes val title: Int,
    @param:StringRes val subtitle: Int,
    val icon: ImageVector,
    val route: String
)
