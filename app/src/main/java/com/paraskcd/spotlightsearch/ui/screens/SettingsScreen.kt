package com.paraskcd.spotlightsearch.ui.screens

import android.graphics.drawable.Drawable
import androidx.activity.compose.LocalActivity
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paraskcd.spotlightsearch.SettingsViewModel
import com.paraskcd.spotlightsearch.ui.pages.settings.personalization.colorpicker.ColorPickerPage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.FeaturesPage
import com.paraskcd.spotlightsearch.ui.pages.settings.HomePage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.manageapps.ManageAppsPage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.manageapps.blacklistapps.BlacklistAppsPage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.managecontacts.ManageContactsPage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.managewebsuggestions.ManageWebSuggestions
import com.paraskcd.spotlightsearch.ui.pages.settings.personalization.PersonalizationPage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.quicksearch.QuicksearchPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val activity = LocalActivity.current
    val navController = rememberNavController()
    val repoVm: SettingsViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            )
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "settings_home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("settings_home") {
                HomePage(navController = navController)
            }
            composable("settings_personalization") {
                PersonalizationPage(navController = navController, repo = repoVm.userRepo)
            }
            composable("settings_color_picker/{key}") { backStackEntry ->
                val key = backStackEntry.arguments?.getString("key").orEmpty()
                ColorPickerPage(navController = navController, keyRaw = key, repo = repoVm.userRepo)
            }
            composable("settings_features") {
                FeaturesPage(navController = navController)
            }
            composable("settings_quick_search") {
                QuicksearchPage(navController = navController, vm = repoVm)
            }
            composable("settings_manage_apps") {
                ManageAppsPage(navController = navController, vm = repoVm)
            }
            composable("settings_web_suggestions") {
                ManageWebSuggestions(navController = navController, vm = repoVm)
            }
            composable("settings_manage_contacts") {
                ManageContactsPage(navController = navController, vm = repoVm)
            }
            composable("settings_apps_blacklist") {
                BlacklistAppsPage(navController = navController, vm = repoVm)
            }
        }
    }
}

data class QuickSearchUi(
    val packageName: String,
    val label: String,
    val enabled: Boolean,
    val sortOrder: Int,
    val icon: Drawable?
)


