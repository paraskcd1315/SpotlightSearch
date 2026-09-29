package com.paraskcd.spotlightsearch.preferences.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn
import com.paraskcd.spotlightsearch.preferences.domain.repository.ThemeRepository
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.search.domain.model.AppIconChoice
import com.paraskcd.spotlightsearch.search.domain.model.IconPack
import com.paraskcd.spotlightsearch.search.domain.ports.AppIconPort
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack.IconPackCatalog
import com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack.IconPackSource
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AppIconPickerViewModel @Inject constructor(
    savedState: SavedStateHandle,
    installedApps: InstalledAppsRepository,
    iconPackCatalog: IconPackCatalog,
    private val iconPackSource: IconPackSource,
    private val appIconPort: AppIconPort,
    themes: ThemeRepository,
    val icons: AppIconLoader
) : ViewModel() {
    val app = AppKey(
        packageName = savedState.get<String>(SettingsRoute.APP_ICON_PACKAGE_ARG).orEmpty(),
        profile = savedState.get<String>(SettingsRoute.APP_ICON_PROFILE_ARG)?.toLongOrNull()?.let(ProfileColumn::profileOf)
    )

    private val picked = MutableStateFlow<String?>(null)

    val label: StateFlow<String> = installedApps.allApps
        .map { list -> list?.firstOrNull { it.key == app }?.label ?: app.packageName }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), app.packageName)

    val packs: StateFlow<List<IconPack>?> = flow { emit(iconPackCatalog.installed()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    val choice: StateFlow<AppIconChoice?> = appIconPort.choices()
        .map { it[app] }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    val pack: StateFlow<IconPack?> = combine(packs, picked, choice, themes.settings()) { list, picked, choice, settings ->
        val wanted = picked ?: choice?.iconPack ?: settings.iconPack
        list?.firstOrNull { it.packageName == wanted } ?: list?.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    val names: StateFlow<List<String>?> = pack
        .mapLatest { selected -> selected?.let { iconPackSource.iconNames(it.packageName) }.orEmpty() }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    init {
        installedApps.warmUp()
    }

    fun selectPack(iconPack: IconPack) {
        picked.value = iconPack.packageName
    }

    fun choose(drawable: String) {
        val selected = pack.value ?: return
        viewModelScope.launch { appIconPort.choose(app, AppIconChoice(selected.packageName, drawable)) }
    }

    fun reset() {
        viewModelScope.launch { appIconPort.choose(app, null) }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
