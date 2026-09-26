package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.domain.ports.SearchConfigPort
import com.paraskcd.spotlightsearch.sources.domain.calculator.Calculator
import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
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
        val settings = config.config().first()
        val board = SectionBoard { send(it) }

        board.put(SectionKind.WEB, listOf(WebSearchHit(query)))
        if (!contacts.hasPermission()) board.put(SectionKind.PERMISSIONS, listOf(ContactsPermissionHit))

        val appHits = async { if (settings.appsEnabled) apps.search(query) else emptyList() }
        val contactHits = async { if (settings.contactsEnabled) contacts.search(query) else emptyList() }

        val localSources = listOf(
            launch { board.put(SectionKind.APPS, appHits.await()) },
            launch { board.put(SectionKind.CONTACTS, contactHits.await()) },
            launch { board.put(SectionKind.SETTINGS, deviceSettings.search(query)) },
            launch { board.put(SectionKind.QUICK_SEARCH, quickSearch.targets(query)) },
            launch { spelling.correction(query)?.let { board.put(SectionKind.DICTIONARY, listOf(SpellingHit(it))) } },
            launch {
                val local = withContext(Dispatchers.Default) { calculator.evaluate(query) } ?: return@launch
                board.update(SectionKind.CALCULATOR) { current -> if (current.isWebCalculation()) current else listOf(local) }
            }
        )

        launch { translation.translate(query)?.let { board.put(SectionKind.TRANSLATION, listOf(it)) } }
        if (settings.webSuggestionsEnabled) launch {
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
    }
}
