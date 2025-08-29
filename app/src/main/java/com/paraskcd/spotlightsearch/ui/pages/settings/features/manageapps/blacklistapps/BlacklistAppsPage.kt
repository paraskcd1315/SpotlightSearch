package com.paraskcd.spotlightsearch.ui.pages.settings.features.manageapps.blacklistapps

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.SettingsViewModel
import com.paraskcd.spotlightsearch.types.SearchResult
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon
import com.paraskcd.spotlightsearch.ui.modifiers.drawFadingEdgesBasic

@Composable
fun BlacklistAppsPage(
    navController: NavController,
    vm: SettingsViewModel
) {
    val apps: List<SearchResult> = remember { vm.appRepo.getAllCachedApps() }
    val blacklisted by vm.blacklistedPackages.collectAsState()

    var query by rememberSaveable { mutableStateOf("") }

    val sorted = remember(apps) { apps.sortedBy { it.title.lowercase() } }
    val filtered = remember(sorted, query) {
        val q = query.trim()
        if (q.isEmpty()) sorted
        else sorted.filter { r ->
            r.title.contains(q, ignoreCase = true) || (r.subtitle?.contains(q, ignoreCase = true) == true)
        }
    }

    val scrollableState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Apps Blacklist",
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().drawFadingEdgesBasic(scrollableState),
                state = scrollableState,
            ) {
                item {
                    Spacer(Modifier.height(64.dp))
                }
                item {
                    if (filtered.isEmpty()) {
                        Text(
                            text = "No results found",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                itemsIndexed(filtered, key = { _, item -> item.subtitle ?: item.title}) { index, item ->
                    val pkg = item.subtitle ?: return@itemsIndexed

                    val shape = when {
                        filtered.size == 1 -> RoundedCornerShape(24.dp)
                        index == 0 -> RoundedCornerShape(
                            topStart = 24.dp, topEnd = 24.dp,
                            bottomStart = 8.dp, bottomEnd = 8.dp
                        )
                        index == filtered.size - 1 -> RoundedCornerShape(
                            topStart = 8.dp, topEnd = 8.dp,
                            bottomStart = 24.dp, bottomEnd = 24.dp
                        )
                        else -> RoundedCornerShape(8.dp)
                    }

                    BaseRowContainer(shape = shape) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RowWithIcon(
                                iconDrawable = item.icon,
                                text = item.title,
                                subtext = pkg
                            )
                        }
                        Switch(
                            checked = blacklisted.contains(pkg),
                            onCheckedChange = { checked ->
                                vm.setAppBlacklisted(pkg, checked)
                            }
                        )
                    }
                }
            }
            TextField(
                value = query,
                onValueChange = {
                    query = it
                },
                singleLine = true,
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100.dp)
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 24.dp, end = 8.dp)
                    )
                },
            )
        }

    }
}