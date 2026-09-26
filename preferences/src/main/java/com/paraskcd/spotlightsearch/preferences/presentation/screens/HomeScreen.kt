package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Palette
import com.composables.icons.lucide.Sparkles
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsGroup
import com.paraskcd.spotlightsearch.preferences.presentation.components.VersionRow
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute

@Composable
fun HomeScreen(onNavigate: (String) -> Unit, onBack: () -> Unit) {
    val pages = listOf(
        SettingPageItem(R.string.home_appearance_title, R.string.home_appearance_subtitle, Lucide.Palette, SettingsRoute.PERSONALIZATION),
        SettingPageItem(R.string.home_features_title, R.string.home_features_subtitle, Lucide.Sparkles, SettingsRoute.FEATURES),
        SettingPageItem(R.string.home_about_title, R.string.home_about_subtitle, Lucide.Info)
    )
    SpScreenScaffold(
        title = stringResource(R.string.settings_title),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack
    ) {
        item { SettingsGroup(pages) { page -> page.route?.also(onNavigate) != null } }
        item { Spacer(Modifier.height(SpMetrics.sectionGap)) }
        item { VersionRow() }
    }
}
