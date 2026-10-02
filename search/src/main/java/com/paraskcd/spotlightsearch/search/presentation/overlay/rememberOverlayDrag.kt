package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity

@Composable
fun rememberOverlayDrag(motion: OverlayMotion, onShown: (Float) -> Unit, onDismissed: () -> Unit): OverlayDrag {
    val shown by rememberUpdatedState(onShown)
    val dismissed by rememberUpdatedState(onDismissed)
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val travelPx = with(density) { OverlayMetrics.DismissTravel.toPx() }
    val flingVelocity = with(density) { OverlayMetrics.DismissFling.toPx() }
    return remember(motion, travelPx, flingVelocity) {
        OverlayDrag(scope, motion, travelPx, flingVelocity, onShown = { shown(it) }, onDismissed = { dismissed() })
    }
}
