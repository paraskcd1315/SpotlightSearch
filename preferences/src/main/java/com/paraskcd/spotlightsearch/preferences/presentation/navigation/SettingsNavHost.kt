package com.paraskcd.spotlightsearch.preferences.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paraskcd.spotlightsearch.designsystem.icons.PersonBook
import com.paraskcd.spotlightsearch.designsystem.icons.WebTraffic
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.presentation.screens.BlacklistAppsScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ColorPickerScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.FeaturesScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.HomeScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ManageAppsScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.PersonalizationScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.QuickSearchScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ToggleScreen
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.SearchSourcesViewModel
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNavHost(themeViewModel: ThemeViewModel, onClose: () -> Unit) {
    val navController = rememberNavController()
    val navigate: (String) -> Unit = { navController.navigate(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.settings_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SettingsRoute.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SettingsRoute.HOME) { HomeScreen(navigate) }
            composable(SettingsRoute.PERSONALIZATION) { PersonalizationScreen(themeViewModel, navigate) }
            composable(SettingsRoute.COLOR_PICKER) { entry ->
                val key = entry.arguments?.getString(SettingsRoute.COLOR_PICKER_ARG)
                    ?.let { raw -> ColorOverrideKey.entries.firstOrNull { it.name == raw } }
                    ?: return@composable
                ColorPickerScreen(key, themeViewModel) { navController.popBackStack() }
            }
            composable(SettingsRoute.FEATURES) { FeaturesScreen(navigate) }
            composable(SettingsRoute.QUICK_SEARCH) { QuickSearchScreen(hiltViewModel()) }
            composable(SettingsRoute.MANAGE_APPS) { ManageAppsScreen(hiltViewModel(), navigate) }
            composable(SettingsRoute.APPS_BLACKLIST) { BlacklistAppsScreen(hiltViewModel()) }
            composable(SettingsRoute.WEB_SUGGESTIONS) {
                val viewModel: SearchSourcesViewModel = hiltViewModel()
                val config by viewModel.config.collectAsState()
                ToggleScreen(
                    title = stringResource(R.string.web_section),
                    label = stringResource(R.string.web_enable),
                    icon = WebTraffic,
                    checked = config?.webSuggestionsEnabled,
                    onCheckedChange = viewModel::setWebSuggestions
                )
            }
            composable(SettingsRoute.MANAGE_CONTACTS) {
                val viewModel: SearchSourcesViewModel = hiltViewModel()
                val config by viewModel.config.collectAsState()
                ToggleScreen(
                    title = stringResource(R.string.contacts_section),
                    label = stringResource(R.string.contacts_enable),
                    icon = PersonBook,
                    checked = config?.contactsEnabled,
                    onCheckedChange = viewModel::setContacts
                )
            }
        }
    }
}
