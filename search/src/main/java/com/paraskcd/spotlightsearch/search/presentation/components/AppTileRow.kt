package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit

@Composable
fun AppTileRow(apps: List<AppHit>, icons: IconSources, callbacks: HitCallbacks) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SearchMetrics.ListPadding)
    ) {
        apps.forEach { app ->
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                AppTile(app, icons.apps, onClick = { callbacks.onHitClick(app) }, onAction = callbacks.onAction)
            }
        }
        repeat(SearchLimits.APPS_PER_ROW - apps.size) { Spacer(modifier = Modifier.weight(1f)) }
    }
}
