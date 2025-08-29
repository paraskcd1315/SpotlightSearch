package com.paraskcd.spotlightsearch.ui.pages.settings.features.managewebsuggestions

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.SettingsViewModel
import com.paraskcd.spotlightsearch.icons.WebTraffic
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.GroupSurface
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon
@Composable
fun ManageWebSuggestions(navController: NavController, vm: SettingsViewModel) {
    val state = vm.globalSearchConfigState.collectAsState().value ?: return
    val context = LocalContext.current
    LazyColumn {
        item {
            Text(
                "Manage Web Suggestions",
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        item {
//            val rows = listOf("globalSwitch", "slider")
            val rows = listOf("globalSwitch")
            GroupSurface(rows.size) { i, shape ->
                when(rows[i]) {
                    "globalSwitch" -> {
                        BaseRowContainer(shape = shape, onClick = { vm.setWebSuggestions(!state.webSuggestionsEnabled) }) {
                            RowWithIcon(
                                icon = WebTraffic,
                                text = "Enable Web Suggestions"
                            )
                            Switch(checked = state.webSuggestionsEnabled, onCheckedChange = { vm.setWebSuggestions(it) })
                        }
                    }
                }
            }
        }
    }
}