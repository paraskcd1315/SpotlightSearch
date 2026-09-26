package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spPanelSurface
import com.paraskcd.spotlightsearch.search.presentation.components.ResultsSkeleton
import com.paraskcd.spotlightsearch.search.presentation.components.SearchResultsPanel
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.model.SearchResults

@Composable
fun ResultsWindow(
    results: SearchResults,
    offsetY: Int,
    heightPx: Int,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    onClose: () -> Unit
) {
    val hasContent = results.sections.isNotEmpty() || results.loading
    var retained by remember { mutableStateOf(results) }
    SideEffect { if (hasContent) retained = results }
    val shown = if (hasContent) results else retained
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.WindowCornerRadius,
        onDismissRequest = onClose,
        visible = hasContent,
        animateIn = true,
        heightPx = heightPx
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .spPanelSurface(RoundedCornerShape(OverlayMetrics.WindowCornerRadius), blurred = blurEnabled),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (shown.sections.isEmpty()) {
                ResultsSkeleton(blurEnabled)
            } else {
                SearchResultsPanel(shown.sections, blurEnabled, icons, callbacks)
            }
        }
    }
}
