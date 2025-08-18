package com.paraskcd.spotlightsearch.ui.pages.settings.features

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.icons.Apps
import com.paraskcd.spotlightsearch.icons.ArrowOutward
import com.paraskcd.spotlightsearch.icons.Assistant
import com.paraskcd.spotlightsearch.icons.ChevronRight
import com.paraskcd.spotlightsearch.icons.FeaturesFunctionality
import com.paraskcd.spotlightsearch.icons.PersonBook
import com.paraskcd.spotlightsearch.icons.WebTraffic
import com.paraskcd.spotlightsearch.icons.Widgets
import com.paraskcd.spotlightsearch.types.SettingPageItem
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.GroupSurface
import com.paraskcd.spotlightsearch.ui.components.HeaderCard
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon

@Composable
fun FeaturesPage(
    navController: NavController,
) {
    val context = LocalContext.current

    val settingsFeature = listOf(
        SettingPageItem(
            title = "Quick search apps",
            subtitle = "Click to manage apps to search query quickly",
            icon = Icons.Default.Search,
            route = "settings_quick_search"
        ),
        SettingPageItem(
            title = "Manage apps",
            subtitle = "Click to manage apps to search, blacklist apps to hide, etc.",
            icon = Apps,
            route = "settings_manage_apps"
        ),
        SettingPageItem(
            title = "Manage web suggestions",
            subtitle = "Click to manage web suggestions from Google",
            icon = WebTraffic,
            route = "settings_web_suggestions"
        ),
        SettingPageItem(
            title = "Manage contacts",
            subtitle = "Click to manage contacts",
            icon = PersonBook,
            route = "settings_manage_contacts"
        )
    )

    val settingsShortcuts = listOf(
        SettingPageItem(
            title = "Search widget",
            subtitle = "Click to place a widget on your home screen",
            icon = Widgets,
            onClick = { }
        ),
        SettingPageItem(
            title = "Digital Assistant",
            subtitle = "Set spotlight search as your digital assistant",
            icon = Assistant,
            onClick = {
                val intents = listOf(
                    Intent(Settings.ACTION_VOICE_INPUT_SETTINGS),
                    Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                )
                val launched = intents.firstOrNull {
                    it.resolveActivity(context.packageManager) != null
                }?.let {
                    context.startActivity(it)
                    true
                } ?: false
                if (!launched) {
                    Toast.makeText(
                        context,
                        "Couldn't open settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    )

    LazyColumn {
        item {
            HeaderCard("Features and Functionality", icon = FeaturesFunctionality)
        }
        item {
            Text(
                "Features",
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        item {
            GroupSurface(count = settingsFeature.size) { i, shape ->
                val setting = settingsFeature[i]
                BaseRowContainer(
                    shape = shape,
                    onClick = {
                        val destinationExists = navController.graph.findNode(setting.route) != null
                        if (destinationExists && setting.route?.isNotEmpty() == true) {
                            navController.navigate(setting.route)
                        } else {
                            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    RowWithIcon(
                        icon = setting.icon,
                        text = setting.title,
                        subtext = setting.subtitle
                    )
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "Go to ${setting.title}"
                    )
                }
            }
        }
        item {
            Text(
                "Shortcuts",
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        item {
            GroupSurface(count = settingsShortcuts.size) { i, shape ->
                val setting = settingsShortcuts[i]
                BaseRowContainer(
                    shape = shape,
                    onClick = {
                        if (setting.onClick == null) {
                            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                            return@BaseRowContainer
                        }
                        setting.onClick()
                    }
                ) {
                    RowWithIcon(
                        icon = setting.icon,
                        text = setting.title,
                        subtext = setting.subtitle
                    )
                    Icon(
                        imageVector = ArrowOutward,
                        contentDescription = "Go to ${setting.title}"
                    )
                }
            }
        }
    }
}