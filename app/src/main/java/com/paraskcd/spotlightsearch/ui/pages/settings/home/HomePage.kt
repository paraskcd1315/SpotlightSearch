package com.paraskcd.spotlightsearch.ui.pages.settings.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.icons.DashboardCustomize
import com.paraskcd.spotlightsearch.icons.FeaturesFunctionality
import com.paraskcd.spotlightsearch.types.SettingPageItem
import com.paraskcd.spotlightsearch.ui.components.ChevronRight
import kotlin.div
import kotlin.math.max

@Composable
fun HomePage(navController: NavController) {
    val settingPages = listOf(
        SettingPageItem(
            title = "Appearance and Personalization",
            subtitle = "Customize the look and feel of Spotlight Search",
            icon = DashboardCustomize,
            route = "settings_personalization"
        ),
        SettingPageItem(
            title = "Features and Functionality",
            subtitle = "Manage features and functionality of Spotlight Search",
            icon = FeaturesFunctionality,
            route = "settings_personalization"
        ),
        SettingPageItem(
            title = "About",
            subtitle = "Learn more about Spotlight Search",
            icon = Icons.Outlined.Info,
            route = "settings_personalization"
        )
    )

    val itemCount = settingPages.size

    LazyColumn {
        item {
            var size by remember { mutableStateOf(IntSize.Zero) }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                color = Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .onSizeChanged { size = it }
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.95f),
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.55f),
                                    MaterialTheme.colorScheme.surface
                                ),
                                center = Offset(
                                    (size.width / 2f),
                                    (size.height / 2f)
                                ),
                                radius = (max(size.width, size.height)
                                    .coerceAtLeast(1))   // evita 0
                                    .toFloat() * 0.95f
                            )
                        )
                        .padding(72.dp) // mismo padding interno
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search Icon",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(40.dp)
                                .width(40.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        Text("Spotlight Search", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.inverseOnSurface)
                    }
                }
            }
        }
        itemsIndexed(settingPages) { i, setting ->
            val shape = when {
                itemCount == 1 -> RoundedCornerShape(24.dp)
                i == 0 -> RoundedCornerShape(
                    topStart = 24.dp, topEnd = 24.dp,
                    bottomStart = 8.dp, bottomEnd = 8.dp
                )
                i == itemCount - 1 -> RoundedCornerShape(
                    topStart = 8.dp, topEnd = 8.dp,
                    bottomStart = 24.dp, bottomEnd = 24.dp
                )
                else -> RoundedCornerShape(8.dp)
            }

            Surface(
                onClick = { navController.navigate(setting.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 1.dp),
                shape = shape,
                color = MaterialTheme.colorScheme.surfaceBright,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(40.dp)
                                .width(40.dp),
                            color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = setting.icon,
                                contentDescription = setting.title,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize()
                            )
                        }
                        Column {
                            Text(setting.title, style = MaterialTheme.typography.titleMedium)
                            setting.subtitle?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                        }
                    }
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "Go to ${setting.title}"
                    )
                }
            }
        }
    }
}