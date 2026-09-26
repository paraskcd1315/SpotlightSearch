package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.presentation.model.HitText
import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
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

@Composable
fun hitText(hit: SearchHit): HitText = when (hit) {
    is AppHit -> HitText(hit.label, hit.packageName, hit.matches)
    is ContactHit -> HitText(hit.name, hit.number, hit.matches)
    is CalculationHit -> HitText(hit.answer, hit.detail ?: stringResource(hit.kind.subtitleRes()))
    is TranslationHit -> HitText(
        hit.translation,
        stringResource(
            R.string.hit_translation_subtitle,
            languageDisplayName(hit.sourceLanguage),
            languageDisplayName(hit.targetLanguage)
        )
    )
    is SuggestionHit -> HitText(hit.text, stringResource(R.string.hit_suggestion_subtitle))
    is SpellingHit -> HitText(stringResource(R.string.hit_spelling_title), hit.suggestion)
    is DeviceSettingHit -> {
        val title = stringResource(hit.setting.titleRes())
        HitText(title, stringResource(R.string.hit_setting_subtitle, title))
    }
    is QuickSearchHit -> HitText(
        stringResource(hit.service.titleRes(), hit.query),
        stringResource(hit.service.nameRes())
    )
    is WebSearchHit -> HitText(
        stringResource(R.string.hit_web_title, hit.query),
        if (hit.engine == WebSearchEngine.SYSTEM) stringResource(R.string.hit_web_subtitle) else stringResource(hit.engine.nameRes())
    )
    ContactsPermissionHit -> HitText(
        stringResource(R.string.hit_permission_title),
        stringResource(R.string.hit_permission_subtitle)
    )
}

private fun CalculationKind.subtitleRes(): Int = when (this) {
    CalculationKind.ARITHMETIC -> R.string.hit_calculation_arithmetic
    CalculationKind.UNIT -> R.string.hit_calculation_unit
    CalculationKind.TEMPERATURE -> R.string.hit_calculation_temperature
    CalculationKind.DATE -> R.string.hit_calculation_date
    CalculationKind.WEB -> R.string.hit_calculation_web
}
