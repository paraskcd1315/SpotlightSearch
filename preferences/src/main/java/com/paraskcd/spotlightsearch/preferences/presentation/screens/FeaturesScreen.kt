package com.paraskcd.spotlightsearch.preferences.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.HeaderCard
import com.paraskcd.spotlightsearch.designsystem.icons.Apps
import com.paraskcd.spotlightsearch.designsystem.icons.ArrowOutward
import com.paraskcd.spotlightsearch.designsystem.icons.Assistant
import com.paraskcd.spotlightsearch.designsystem.icons.ChevronRight
import com.paraskcd.spotlightsearch.designsystem.icons.FeaturesFunctionality
import com.paraskcd.spotlightsearch.designsystem.icons.PersonBook
import com.paraskcd.spotlightsearch.designsystem.icons.WebTraffic
import com.paraskcd.spotlightsearch.designsystem.icons.Widgets
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.infrastructure.system.AssistantSettings
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsGroup
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute

@Composable
fun FeaturesScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val openFailed = stringResource(R.string.settings_open_failed)
    val features = listOf(
        SettingPageItem(R.string.features_quick_search_title, R.string.features_quick_search_subtitle, Icons.Default.Search, SettingsRoute.QUICK_SEARCH),
        SettingPageItem(R.string.features_manage_apps_title, R.string.features_manage_apps_subtitle, Apps, SettingsRoute.MANAGE_APPS),
        SettingPageItem(R.string.features_web_title, R.string.features_web_subtitle, WebTraffic, SettingsRoute.WEB_SUGGESTIONS),
        SettingPageItem(R.string.features_contacts_title, R.string.features_contacts_subtitle, PersonBook, SettingsRoute.MANAGE_CONTACTS)
    )
    val widget = SettingPageItem(R.string.features_widget_title, R.string.features_widget_subtitle, Widgets)
    val assistant = SettingPageItem(R.string.features_assistant_title, R.string.features_assistant_subtitle, Assistant)

    LazyColumn {
        item { HeaderCard(stringResource(R.string.features_header), icon = FeaturesFunctionality) }
        item { SectionTitle(stringResource(R.string.features_section)) }
        item {
            SettingsGroup(features, ChevronRight) { page -> page.route?.also(onNavigate) != null }
        }
        item { SectionTitle(stringResource(R.string.features_shortcuts_section)) }
        item {
            SettingsGroup(listOf(widget, assistant), ArrowOutward) { page ->
                if (page != assistant) return@SettingsGroup false
                if (!AssistantSettings.open(context)) Toast.makeText(context, openFailed, Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}
