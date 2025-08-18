package com.paraskcd.spotlightsearch.ui.screens

import android.graphics.drawable.Drawable
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paraskcd.spotlightsearch.data.entities.QuickSearchProviderEntity
import com.paraskcd.spotlightsearch.data.repo.GlobalSearchConfigRepository
import com.paraskcd.spotlightsearch.data.repo.QuickSearchProviderRepository
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.providers.AppRepositoryProvider
import com.paraskcd.spotlightsearch.ui.pages.settings.colorpicker.ColorPickerPage
import com.paraskcd.spotlightsearch.ui.pages.settings.features.FeaturesPage
import com.paraskcd.spotlightsearch.ui.pages.settings.home.HomePage
import com.paraskcd.spotlightsearch.ui.pages.settings.manageapps.ManageAppsPage
import com.paraskcd.spotlightsearch.ui.pages.settings.managecontacts.ManageContactsPage
import com.paraskcd.spotlightsearch.ui.pages.settings.managewebsuggestions.ManageWebSuggestions
import com.paraskcd.spotlightsearch.ui.pages.settings.personalization.PersonalizationPage
import com.paraskcd.spotlightsearch.ui.pages.settings.quicksearch.QuicksearchPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

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
                QuicksearchPage(navController = navController)
            }
            composable("settings_manage_apps") {
                ManageAppsPage(navController = navController)
            }
            composable("settings_web_suggestions") {
                ManageWebSuggestions(navController = navController)
            }
            composable("settings_manage_contacts") {
                ManageContactsPage(navController = navController)
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


@HiltViewModel
class SettingsRepoViewModel @Inject constructor(
    val userRepo: UserThemeRepository,
    val quickSearchRepo: QuickSearchProviderRepository,
    val appRepo: AppRepositoryProvider,
    val globalSearchConfigRepo: GlobalSearchConfigRepository
) : ViewModel() {
    private val labelMap = mapOf(
        "com.google.android.googlequicksearchbox" to "Google",
        "com.google.android.youtube" to "YouTube",
        "com.google.android.apps.youtube.music" to "YouTube Music",
        "com.google.android.apps.maps" to "Google Maps",
        "com.android.vending" to "Play Store",
        "com.instagram.barcelona" to "Threads",
        "com.linkedin.android" to "LinkedIn",
        "com.twitter.android" to "X (Twitter)",
        "com.facebook.katana" to "Facebook"
    )

    val quickSearchProviders = quickSearchRepo.observe()
        .map { list ->
            list.sortedBy { it.sortOrder }
                .map { it.toQuickSearchUi() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun QuickSearchProviderEntity.toQuickSearchUi() =
        QuickSearchUi(packageName, labelMap[packageName] ?: packageName, enabled, sortOrder, getCachedAppIcon(packageName))

    fun toggle(pkg: String, enabled: Boolean) = viewModelScope.launch {
        quickSearchRepo.toggle(pkg, enabled)
    }

    fun reorder(newList: List<QuickSearchUi>) = viewModelScope.launch {
        quickSearchRepo.reorder(newList.map { it.packageName })
    }

    private fun getCachedAppIcon(pkg: String): Drawable? = appRepo.getCachedApp(pkg)?.icon

    val globalSearchConfigState = globalSearchConfigRepo.config.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), null)

    fun setApps(enabled: Boolean) = viewModelScope.launch { globalSearchConfigRepo.setAppsEnabled(enabled) }
    fun setContacts(enabled: Boolean) = viewModelScope.launch { globalSearchConfigRepo.setContactsEnabled(enabled) }
    fun setWebSuggestions(enabled: Boolean) = viewModelScope.launch { globalSearchConfigRepo.setWebSuggestionsEnabled(enabled) }
}