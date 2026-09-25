package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.QuickSearchHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

@Composable
fun HitContent(hit: SearchHit, icons: IconSources, callbacks: HitCallbacks) {
    val onClick = { callbacks.onHitClick(hit) }
    when (hit) {
        is AppHit -> AppRow(hit, icons.apps, onClick, callbacks.onAction)
        is ContactHit -> ContactRow(hit, icons.photos, onClick, callbacks.onAction)
        is QuickSearchHit -> HitRow(hit, onClick) {
            AppIconImage(hit.service.packageName, icons.apps, themed = false, size = SearchMetrics.RowIconSize)
        }
        else -> HitRow(hit, onClick)
    }
}
