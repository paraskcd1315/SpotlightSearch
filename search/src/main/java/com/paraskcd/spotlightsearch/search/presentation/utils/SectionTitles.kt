package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.annotation.StringRes
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind

@StringRes
fun SectionKind.titleRes(): Int = when (this) {
    SectionKind.FREQUENT -> R.string.section_frequent
    SectionKind.TOP_HIT -> R.string.section_top_hit
    SectionKind.SETTINGS -> R.string.section_settings
    SectionKind.DICTIONARY -> R.string.section_dictionary
    SectionKind.PERMISSIONS -> R.string.section_permissions
    SectionKind.CALCULATOR -> R.string.section_calculator
    SectionKind.APPS -> R.string.section_apps
    SectionKind.CONTACTS -> R.string.section_contacts
    SectionKind.TRANSLATION -> R.string.section_translation
    SectionKind.SUGGESTIONS -> R.string.section_suggestions
    SectionKind.QUICK_SEARCH -> R.string.section_quick_search
}
