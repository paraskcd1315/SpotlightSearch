package com.paraskcd.spotlightsearch

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.data.entities.QuickSearchProviderEntity
import com.paraskcd.spotlightsearch.data.repo.BlacklistAppsRepository
import com.paraskcd.spotlightsearch.data.repo.GlobalSearchConfigRepository
import com.paraskcd.spotlightsearch.data.repo.QuickSearchProviderRepository
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import com.paraskcd.spotlightsearch.providers.AppRepositoryProvider
import com.paraskcd.spotlightsearch.ui.screens.QuickSearchUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val userRepo: UserThemeRepository,
    val quickSearchRepo: QuickSearchProviderRepository,
    val appRepo: AppRepositoryProvider,
    val globalSearchConfigRepo: GlobalSearchConfigRepository,
    val blacklistRepo: BlacklistAppsRepository
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

    val blacklistedPackages = blacklistRepo.observe()
        .map { list -> list.map { it.packageName }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun setAppBlacklisted(packageName: String, blacklisted: Boolean) = viewModelScope.launch {
        if (blacklisted) blacklistRepo.insert(packageName) else blacklistRepo.delete(packageName)
    }
}