package com.paraskcd.spotlightsearch.ui.pages.settings

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.icons.FeaturesFunctionality
import com.paraskcd.spotlightsearch.types.SettingPageItem
import com.paraskcd.spotlightsearch.icons.ChevronRight
import com.paraskcd.spotlightsearch.icons.Palette
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.GroupSurface
import com.paraskcd.spotlightsearch.ui.components.HeaderCard
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon

@Composable
fun HomePage(navController: NavController) {
    val settingPages = listOf(
        SettingPageItem(
            title = "Appearance and Personalization",
            subtitle = "Customize the look and feel of Spotlight Search",
            icon = Palette,
            route = "settings_personalization"
        ),
        SettingPageItem(
            title = "Features and Functionality",
            subtitle = "Manage features and functionality of Spotlight Search",
            icon = FeaturesFunctionality,
            route = "settings_features"
        ),
        SettingPageItem(
            title = "About",
            subtitle = "Learn more about Spotlight Search",
            icon = Icons.Outlined.Info,
            route = "settings_about"
        )
    )

    val itemCount = settingPages.size
    val context = LocalContext.current

    LazyColumn {
        item {
            HeaderCard("Spotlight Search", icon = Icons.Outlined.Search)
        }
        item {
            GroupSurface(count = itemCount) { i, shape ->
                val setting = settingPages[i]
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
    }
}