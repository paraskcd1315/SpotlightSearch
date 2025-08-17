package com.paraskcd.spotlightsearch.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
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
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.ui.pages.settings.colorpicker.ColorPickerPage
import com.paraskcd.spotlightsearch.ui.pages.settings.home.HomePage
import com.paraskcd.spotlightsearch.ui.pages.settings.personalization.PersonalizationPage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val activity = LocalActivity.current
    val navController = rememberNavController()
    val repoVm: SettingsRepoViewModel = hiltViewModel()

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
                HomePage(navController)
            }
            composable("settings_personalization") {
                PersonalizationPage(navController = navController, repo = repoVm.repo)
            }
            composable("settings_color_picker/{key}") { backStackEntry ->
                val key = backStackEntry.arguments?.getString("key").orEmpty()
                ColorPickerPage(navController, key, repoVm.repo)
            }
        }
    }
}

@HiltViewModel
class SettingsRepoViewModel @Inject constructor(
    val repo: UserThemeRepository
) : ViewModel()