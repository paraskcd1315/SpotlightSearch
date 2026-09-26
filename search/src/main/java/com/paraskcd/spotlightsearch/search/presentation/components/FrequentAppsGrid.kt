package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.surfaceAlpha
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FrequentAppsGrid(apps: List<AppHit>, blurEnabled: Boolean, icons: IconSources, callbacks: HitCallbacks) {
    val shape = RoundedCornerShape(DsMetrics.CornerLarge)
    Surface(
        color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = surfaceAlpha(blurEnabled)),
        shape = shape,
        modifier = Modifier
            .padding(vertical = SearchMetrics.RowGap)
            .fillMaxWidth()
            .border(
                DsMetrics.OutlineWidth,
                MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                shape
            )
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = SearchMetrics.TilesPerRow,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            apps.forEach { app ->
                AppTile(app, icons.apps, onClick = { callbacks.onHitClick(app) }, onAction = callbacks.onAction)
            }
        }
    }
}
