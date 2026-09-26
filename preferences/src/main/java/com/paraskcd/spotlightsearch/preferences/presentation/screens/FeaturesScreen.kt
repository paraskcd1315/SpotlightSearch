package com.paraskcd.spotlightsearch.preferences.presentation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Bot
import com.composables.icons.lucide.Contact
import com.composables.icons.lucide.ExternalLink
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.LayoutGrid
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSectionHeader
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.infrastructure.system.AssistantSettings
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsGroup
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute

@Composable
fun FeaturesScreen(onNavigate: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val openFailed = stringResource(R.string.settings_open_failed)
    val features = listOf(
        SettingPageItem(R.string.features_quick_search_title, R.string.features_quick_search_subtitle, Lucide.Search, SettingsRoute.QUICK_SEARCH),
        SettingPageItem(R.string.features_manage_apps_title, R.string.features_manage_apps_subtitle, Lucide.LayoutGrid, SettingsRoute.MANAGE_APPS),
        SettingPageItem(R.string.features_web_title, R.string.features_web_subtitle, Lucide.Globe, SettingsRoute.WEB_SUGGESTIONS),
        SettingPageItem(R.string.features_contacts_title, R.string.features_contacts_subtitle, Lucide.Contact, SettingsRoute.MANAGE_CONTACTS)
    )
    SpScreenScaffold(
        title = stringResource(R.string.features_header),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack
    ) {
        item { SpSectionHeader(stringResource(R.string.features_section)) }
        item { SettingsGroup(features, onNavigate) }
        item { SpSectionHeader(stringResource(R.string.features_shortcuts_section)) }
        item {
            SpGroupedList(count = 1) {
                SpSettingsRow(
                    label = stringResource(R.string.features_assistant_title),
                    caption = stringResource(R.string.features_assistant_subtitle),
                    icon = Lucide.Bot,
                    trailingIcon = Lucide.ExternalLink,
                    onClick = {
                        if (!AssistantSettings.open(context)) Toast.makeText(context, openFailed, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
