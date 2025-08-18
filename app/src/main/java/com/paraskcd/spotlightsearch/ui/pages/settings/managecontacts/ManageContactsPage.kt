package com.paraskcd.spotlightsearch.ui.pages.settings.managecontacts

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paraskcd.spotlightsearch.icons.Apps
import com.paraskcd.spotlightsearch.icons.PersonBook
import com.paraskcd.spotlightsearch.ui.components.BaseRowContainer
import com.paraskcd.spotlightsearch.ui.components.GroupSurface
import com.paraskcd.spotlightsearch.ui.components.RowWithIcon
import com.paraskcd.spotlightsearch.ui.screens.SettingsRepoViewModel

@Composable
fun ManageContactsPage(navController: NavController, vm: SettingsRepoViewModel = hiltViewModel()) {
    val state = vm.globalSearchConfigState.collectAsState().value ?: return
    val context = LocalContext.current
    LazyColumn {
        item {
            Text(
                "Manage Contacts",
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        item {
//            val rows = listOf("globalSwitch", "phoneEnabled", "smsEnabled", "whatsAppEnabled", "addMoreApps")
            val rows = listOf("globalSwitch")
            GroupSurface(rows.size) { i, shape ->
                when(rows[i]) {
                    "globalSwitch" -> {
                        BaseRowContainer(shape = shape, onClick = { vm.setContacts(!state.contactsEnabled) }) {
                            RowWithIcon(
                                icon = PersonBook,
                                text = "Enable contacts"
                            )
                            Switch(checked = state.contactsEnabled, onCheckedChange = { vm.setContacts(it) })
                        }
                    }
                }
            }
        }
    }
}