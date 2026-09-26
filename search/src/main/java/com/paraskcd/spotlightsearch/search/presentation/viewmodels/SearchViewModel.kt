package com.paraskcd.spotlightsearch.search.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.ports.SearchConfigPort
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.domain.usecase.LaunchHitUseCase
import com.paraskcd.spotlightsearch.search.domain.usecase.ObserveFrequentAppsUseCase
import com.paraskcd.spotlightsearch.search.domain.usecase.SearchUseCase
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.infrastructure.icons.ContactPhotoLoader
import com.paraskcd.spotlightsearch.search.presentation.model.HitOutcome
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.model.SearchResults
import com.paraskcd.spotlightsearch.search.presentation.utils.HitActions
import com.paraskcd.spotlightsearch.sources.domain.model.actions.CopyNumber
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SearchWeb
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SpellingHit
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.SpellingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val search: SearchUseCase,
    private val frequentApps: ObserveFrequentAppsUseCase,
    private val launchHit: LaunchHitUseCase,
    searchConfig: SearchConfigPort,
    appIcons: AppIconLoader,
    contactPhotos: ContactPhotoLoader,
    apps: InstalledAppsRepository,
    spelling: SpellingRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")

    val icons = IconSources(appIcons, contactPhotos)

    val config: StateFlow<SearchConfig> = searchConfig.config()
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchConfig())

    val results: StateFlow<SearchResults> = _query
        .map { it.trim() }
        .debounce { if (it.isEmpty()) 0L else QUERY_DEBOUNCE_MS }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val sections = if (query.isBlank()) frequentSections() else search(query)
            sections.map { SearchResults(it, loading = false) }.onStart { emit(SearchResults(loading = true)) }
        }
        .scan(SearchResults()) { previous, next ->
            if (next.loading) previous.copy(loading = true) else next
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchResults())

    init {
        apps.warmUp()
        spelling.warmUp()
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onHitClick(hit: SearchHit): HitOutcome {
        if (hit is SpellingHit) {
            onQueryChange(hit.suggestion)
            return HitOutcome.ReplaceQuery(hit.suggestion)
        }
        val action = HitActions.primary(hit, _query.value.trim(), config.value.searchEngine) ?: return HitOutcome.Stay
        return run(action)
    }

    fun run(action: HitAction): HitOutcome {
        viewModelScope.launch { launchHit(action) }
        return if (action is CopyNumber) HitOutcome.Stay else HitOutcome.Close
    }

    fun submit(): HitOutcome {
        val firstActionable = results.value.sections
            .filter { it.kind in SUBMITTABLE }
            .flatMap { it.hits }
            .firstOrNull()
        val engine = config.value.searchEngine
        val action = firstActionable?.let { HitActions.primary(it, _query.value.trim(), engine) }
            ?: SearchWeb(_query.value.trim(), engine)
        return run(action)
    }

    private fun frequentSections() = frequentApps().map { hits ->
        if (hits.isEmpty()) emptyList() else listOf(SearchSection(SectionKind.FREQUENT, hits))
    }

    private companion object {
        val SUBMITTABLE = setOf(SectionKind.TOP_HIT, SectionKind.SETTINGS, SectionKind.APPS, SectionKind.CONTACTS)
        const val QUERY_DEBOUNCE_MS = 100L
    }
}
