package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
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
    onTopOnScreen: (Int?) -> Unit
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
            SideEffect { onTopOnScreen(null) }
            return@BlurredWindow
        }
        val view = LocalView.current
        val reportTop = Modifier.onGloballyPositioned {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            onTopOnScreen(location[1])
        }
        val appear = remember { MutableTransitionState(false) }.apply { targetState = true }
        AnimatedVisibility(
            visibleState = appear,
            enter = slideInVertically(tween(OverlayMetrics.EntryFadeMs)) { it } + fadeIn(tween(OverlayMetrics.EntryFadeMs))
        ) {
            if (apps.isEmpty()) {
                FrequentAppsSkeleton(blurEnabled, reportTop)
            } else {
                FrequentAppsGrid(apps, blurEnabled, icons, callbacks, reportTop)
            }
        }
    }
}
