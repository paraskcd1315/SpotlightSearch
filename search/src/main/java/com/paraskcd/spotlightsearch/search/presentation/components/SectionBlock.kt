package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources

@Composable
fun SectionBlock(section: SearchSection, blurEnabled: Boolean, icons: IconSources, callbacks: HitCallbacks) {
    Column {
        SectionHeader(section.kind)
        section.hits.forEachIndexed { index, hit ->
            GlassRow(index = index, count = section.hits.size, blurEnabled = blurEnabled) {
                HitContent(hit, icons, callbacks)
            }
        }
    }
}
