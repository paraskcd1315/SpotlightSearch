package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.WindowCornerRadius,
        onDismissRequest = onClose,
        visible = visible
    ) {
        if (!visible) {
            SideEffect { onHeight(null) }
            return@BlurredWindow
        }
        val reportHeight = Modifier.onSizeChanged { onHeight(it.height) }
        if (apps.isEmpty()) {
            FrequentAppsSkeleton(blurEnabled, reportHeight)
        } else {
            FrequentAppsGrid(apps, blurEnabled, icons, callbacks, reportHeight)
        }
    }
}
