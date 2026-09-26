package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import com.paraskcd.spotlightsearch.search.presentation.components.FrequentAppsGrid
import com.paraskcd.spotlightsearch.search.presentation.components.FrequentAppsSkeleton
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit

@Composable
fun FrequentAppsWindow(
    apps: List<AppHit>,
    loading: Boolean,
    offsetY: Int,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    onClose: () -> Unit,
    onHeight: (Int?) -> Unit
) {
    val visible = apps.isNotEmpty() || loading
    var retained by remember { mutableStateOf(apps) }
    SideEffect {
        if (visible) retained = apps
        if (!visible) onHeight(null)
    }
    val shownApps = if (visible) apps else retained
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.WindowCornerRadius,
        onDismissRequest = onClose,
        visible = visible,
        animateIn = true
    ) {
        val reportHeight = Modifier.onSizeChanged { if (visible) onHeight(it.height) }
        if (shownApps.isEmpty()) {
            FrequentAppsSkeleton(blurEnabled, reportHeight)
        } else {
            FrequentAppsGrid(shownApps, blurEnabled, icons, callbacks, reportHeight)
        }
    }
}
