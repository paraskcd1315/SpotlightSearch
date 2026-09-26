package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit

@Composable
fun FrequentAppsGrid(
    apps: List<AppHit>,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    modifier: Modifier = Modifier
) {
    FrequentAppsCard(blurEnabled, modifier) {
        apps.forEach { app ->
            AppTile(app, icons.apps, onClick = { callbacks.onHitClick(app) }, onAction = callbacks.onAction)
        }
    }
}
