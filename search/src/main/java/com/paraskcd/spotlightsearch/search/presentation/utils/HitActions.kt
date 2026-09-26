package com.paraskcd.spotlightsearch.search.presentation.utils

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.LaunchApp
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenContact
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenContactsPermission
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenDeviceSetting
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenTranslator
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SearchWeb
import com.paraskcd.spotlightsearch.sources.domain.model.actions.SearchWith
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactsPermissionHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.DeviceSettingHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.QuickSearchHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SpellingHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SuggestionHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.TranslationHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.WebSearchHit

object HitActions {
    fun primary(hit: SearchHit, query: String, engine: WebSearchEngine): HitAction? = when (hit) {
        is AppHit -> LaunchApp(hit.packageName)
        is ContactHit -> OpenContact(hit.number)
        is CalculationHit -> if (hit.kind == CalculationKind.WEB) SearchWeb(query, engine) else null
        is TranslationHit -> OpenTranslator(hit.text, hit.sourceLanguage, hit.targetLanguage)
        is SuggestionHit -> SearchWeb(hit.text, engine)
        is DeviceSettingHit -> OpenDeviceSetting(hit.setting)
        is QuickSearchHit -> SearchWith(hit.service, hit.query)
        is WebSearchHit -> SearchWeb(hit.query, hit.engine)
        ContactsPermissionHit -> OpenContactsPermission
        is SpellingHit -> null
    }
}
