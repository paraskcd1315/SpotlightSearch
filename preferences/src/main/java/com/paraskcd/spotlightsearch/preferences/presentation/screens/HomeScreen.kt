package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.HeaderCard
import com.paraskcd.spotlightsearch.designsystem.icons.ChevronRight
import com.paraskcd.spotlightsearch.designsystem.icons.FeaturesFunctionality
import com.paraskcd.spotlightsearch.designsystem.icons.Palette
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsGroup
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val pages = listOf(
        SettingPageItem(R.string.home_appearance_title, R.string.home_appearance_subtitle, Palette, SettingsRoute.PERSONALIZATION),
        SettingPageItem(R.string.home_features_title, R.string.home_features_subtitle, FeaturesFunctionality, SettingsRoute.FEATURES),
        SettingPageItem(R.string.home_about_title, R.string.home_about_subtitle, Icons.Outlined.Info)
    )
    LazyColumn {
        item { HeaderCard(stringResource(R.string.home_header), icon = Icons.Outlined.Search) }
        item {
            SettingsGroup(pages, ChevronRight) { page ->
                page.route?.also(onNavigate) != null
            }
        }
    }
}
