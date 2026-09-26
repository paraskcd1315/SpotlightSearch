package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.domain.ports.SearchConfigPort
import com.paraskcd.spotlightsearch.search.domain.ports.UsagePort
import com.paraskcd.spotlightsearch.sources.domain.calculator.Calculator
import com.paraskcd.spotlightsearch.sources.domain.matching.SearchThresholds
import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactsPermissionHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SpellingHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SuggestionHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.WebSearchHit
import com.paraskcd.spotlightsearch.sources.domain.repository.ContactsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.DeviceSettingsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.QuickSearchRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.SpellingRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.SuggestionsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.TranslationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val config: SearchConfigPort,
    private val usage: UsagePort,
    private val apps: InstalledAppsRepository,
    private val contacts: ContactsRepository,
    private val suggestions: SuggestionsRepository,
    private val translation: TranslationRepository,
    private val spelling: SpellingRepository,
    private val quickSearch: QuickSearchRepository,
    private val deviceSettings: DeviceSettingsRepository,
    private val calculator: Calculator
) {
    operator fun invoke(rawQuery: String): Flow<List<SearchSection>> = channelFlow {
        val query = rawQuery.trim()
        if (query.isEmpty()) {
            send(emptyList())
            return@channelFlow
        }
        val short = query.length < SearchThresholds.SHORT_QUERY_LENGTH
        val settings = config.config().first()
        val board = SectionBoard { send(it) }

        board.put(SectionKind.WEB, listOf(WebSearchHit(query)))
        if (!contacts.hasPermission()) board.put(SectionKind.PERMISSIONS, listOf(ContactsPermissionHit))

        val appHits = async {
            if (!settings.appsEnabled) return@async emptyList()
            val ranks = usage.mostUsedPackages(USAGE_WINDOW).first().withIndex().associate { it.value to it.index }
            apps.search(query).sortedWith(compareBy<AppHit> { it.tier }.thenBy { ranks[it.packageName] ?: Int.MAX_VALUE })
        }
        val contactHits = async { if (settings.contactsEnabled) contacts.search(query) else emptyList() }

        val localSources = listOf(
            launch {
                val ranked = TopHit.pick(appHits.await(), contactHits.await())
                board.put(SectionKind.TOP_HIT, listOfNotNull(ranked.top))
                board.put(SectionKind.APPS, ranked.apps)
                board.put(SectionKind.CONTACTS, ranked.contacts)
            },
            launch { board.put(SectionKind.SETTINGS, deviceSettings.search(query)) },
            launch {
                val targets = quickSearch.targets(query)
                board.put(SectionKind.QUICK_SEARCH, if (short) targets.take(1) else targets)
            },
            launch {
                if (query.length < SearchThresholds.SPELLING_MIN_LENGTH) return@launch
                spelling.correction(query)?.let { board.put(SectionKind.DICTIONARY, listOf(SpellingHit(it))) }
            },
            launch {
                val local = withContext(Dispatchers.Default) { calculator.evaluate(query) } ?: return@launch
                board.update(SectionKind.CALCULATOR) { current -> if (current.isWebCalculation()) current else listOf(local) }
            }
        )

        launch { translation.translate(query)?.let { board.put(SectionKind.TRANSLATION, listOf(it)) } }
        if (settings.webSuggestionsEnabled && query.length >= SearchThresholds.WEB_SUGGESTIONS_MIN_LENGTH) launch {
            val found = suggestions.suggest(query)
            val webAnswer = found.firstOrNull { it.trim().startsWith(WEB_ANSWER_PREFIX) }
            if (webAnswer != null) {
                board.put(SectionKind.CALCULATOR, listOf(CalculationHit("$query $webAnswer", CalculationKind.WEB)))
            } else if (appHits.await().isEmpty() && contactHits.await().isEmpty()) {
                board.put(SectionKind.SUGGESTIONS, found.map(::SuggestionHit))
            }
        }

        withTimeoutOrNull(LOCAL_SOURCES_WAIT_MS) { localSources.joinAll() }
        board.release()
    }

    private fun List<*>?.isWebCalculation() =
        this?.any { it is CalculationHit && it.kind == CalculationKind.WEB } == true

    private companion object {
        const val WEB_ANSWER_PREFIX = "="
        const val LOCAL_SOURCES_WAIT_MS = 200L
        const val USAGE_WINDOW = 50
    }
}
