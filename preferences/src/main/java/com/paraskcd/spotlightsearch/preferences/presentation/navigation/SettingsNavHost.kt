package com.paraskcd.spotlightsearch.preferences.presentation.navigation

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Contact
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.presentation.screens.AboutScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.AppIconPickerScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.AppIconsScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.BlacklistAppsScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ColorPickerScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.FeaturesScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.HomeScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ManageAppsScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.PersonalizationScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.QuickSearchScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ResultsSettingsScreen
import com.paraskcd.spotlightsearch.preferences.presentation.screens.ToggleScreen
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.SearchSourcesViewModel
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel

@Composable
fun SettingsNavHost(themeViewModel: ThemeViewModel, onClose: () -> Unit, openRoute: String? = null) {
    val navController = rememberNavController()
    val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var sidebarRoute by rememberSaveable { mutableStateOf(SettingsRoute.PERSONALIZATION) }
    val current by navController.currentBackStackEntryAsState()
    val navigate: (String) -> Unit = { navController.navigate(it) }
    val openFromSidebar: (String) -> Unit = { route ->
        sidebarRoute = route
        navController.navigate(route) { popUpTo(SettingsRoute.HOME) }
    }
    val back: () -> Unit = {
        val leavesToHome = navController.previousBackStackEntry?.destination?.route == SettingsRoute.HOME
        if ((landscape && leavesToHome) || !navController.popBackStack()) onClose()
    }

    LaunchedEffect(openRoute) {
        openRoute?.let { route ->
            navController.navigate(route) { popUpTo(navController.graph.id) { inclusive = true } }
        }
    }

    LaunchedEffect(landscape, current?.destination?.route) {
        if (landscape && current?.destination?.route == SettingsRoute.HOME) {
            navController.navigate(sidebarRoute)
        }
    }

    val push = tween<IntOffset>(SpMotion.durPushMs, easing = SpMotion.easeIos)
    val forward = if (LocalLayoutDirection.current == LayoutDirection.Rtl) -1 else 1

    Row(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.displayCutout.union(WindowInsets.navigationBars).only(WindowInsetsSides.Horizontal))
    ) {
        if (landscape) {
            Box(modifier = Modifier.weight(SettingsMetrics.LandscapeSidebarWeight)) {
                HomeScreen(openFromSidebar, onClose, sidebarRoute)
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = SettingsRoute.HOME,
                enterTransition = { slideInHorizontally(push) { width -> width * forward } },
                exitTransition = { slideOutHorizontally(push) { width -> -width * forward } },
                popEnterTransition = { slideInHorizontally(push) { width -> -width * forward } },
                popExitTransition = { slideOutHorizontally(push) { width -> width * forward } },
                predictivePopEnterTransition = { _ -> slideInHorizontally(push) { width -> -width * forward } },
                predictivePopExitTransition = { _ -> slideOutHorizontally(push) { width -> width * forward } }
            ) {
                composable(SettingsRoute.HOME) { HomeScreen(navigate, onClose) }
                composable(SettingsRoute.PERSONALIZATION) { PersonalizationScreen(themeViewModel, navigate, back) }
                composable(SettingsRoute.COLOR_PICKER) { entry ->
                    val key = entry.arguments?.getString(SettingsRoute.COLOR_PICKER_ARG)
                        ?.let { raw -> ColorOverrideKey.entries.firstOrNull { it.name == raw } }
                        ?: return@composable
                    ColorPickerScreen(key, themeViewModel, back)
                }
                composable(SettingsRoute.FEATURES) { FeaturesScreen(navigate, back) }
                composable(SettingsRoute.ABOUT) { AboutScreen(back) }
                composable(SettingsRoute.RESULTS) { ResultsSettingsScreen(hiltViewModel(), back) }
                composable(SettingsRoute.QUICK_SEARCH) { QuickSearchScreen(hiltViewModel(), back) }
                composable(SettingsRoute.MANAGE_APPS) { ManageAppsScreen(hiltViewModel(), navigate, back) }
                composable(SettingsRoute.APPS_BLACKLIST) { BlacklistAppsScreen(hiltViewModel(), back) }
                composable(SettingsRoute.APP_ICONS) { AppIconsScreen(hiltViewModel(), navigate, back) }
                composable(SettingsRoute.APP_ICON) { AppIconPickerScreen(hiltViewModel(), back) }
                composable(SettingsRoute.WEB_SUGGESTIONS) {
                    val viewModel: SearchSourcesViewModel = hiltViewModel()
                    val config by viewModel.config.collectAsState()
                    ToggleScreen(
                        title = stringResource(R.string.web_section),
                        label = stringResource(R.string.web_enable),
                        icon = Lucide.Globe,
                        checked = config?.webSuggestionsEnabled,
                        onCheckedChange = viewModel::setWebSuggestions,
                        onBack = back
                    )
                }
                composable(SettingsRoute.MANAGE_CONTACTS) {
                    val viewModel: SearchSourcesViewModel = hiltViewModel()
                    val config by viewModel.config.collectAsState()
                    ToggleScreen(
                        title = stringResource(R.string.contacts_section),
                        label = stringResource(R.string.contacts_enable),
                        icon = Lucide.Contact,
                        checked = config?.contactsEnabled,
                        onCheckedChange = viewModel::setContacts,
                        onBack = back
                    )
                }
            }
        }
    }
}
