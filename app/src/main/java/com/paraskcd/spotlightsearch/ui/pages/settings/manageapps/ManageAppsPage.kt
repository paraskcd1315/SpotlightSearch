package com.paraskcd.spotlightsearch.ui.pages.settings.manageapps

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.icons.Apps
import com.paraskcd.spotlightsearch.icons.ChevronRight
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.GroupSurface
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon
import com.paraskcd.spotlightsearch.ui.screens.SettingsRepoViewModel

@Composable
fun ManageAppsPage(navController: NavController, vm: SettingsRepoViewModel = hiltViewModel()) {
    val state = vm.globalSearchConfigState.collectAsState().value ?: return
    val context = LocalContext.current
    LazyColumn {
        item {
            Text(
                "Manage Apps",
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        item {
            val rows = listOf("globalSwitch", "blacklist")
            GroupSurface(rows.size) { i, shape ->
                when (rows[i]) {
                    "globalSwitch" -> {
                        BaseRowContainer(shape = shape, onClick = { vm.setApps(!state.appsEnabled) }) {
                            RowWithIcon(
                                icon = Apps,
                                text = "Enable apps"
                            )
                            Switch(checked = state.appsEnabled, onCheckedChange = { vm.setApps(it) })
                        }
                    }
                    "blacklist" -> {
                        BaseRowContainer(
                            shape = shape,
                            onClick = {
                                val destinationExists = navController.graph.findNode("settings_apps_blacklist") != null
                                if (destinationExists) {
                                    navController.navigate("settings_apps_blacklist")
                                } else {
                                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            RowWithIcon(
                                icon = Apps,
                                text = "Blacklist apps"
                            )
                            Icon(
                                imageVector = ChevronRight,
                                contentDescription = "Go to Blacklist apps"
                            )
                        }
                    }
                }
            }
        }
    }
}