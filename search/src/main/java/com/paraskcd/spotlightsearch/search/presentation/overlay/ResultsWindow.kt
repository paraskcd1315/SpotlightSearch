package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
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
    maxHeight: Dp,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    onClose: () -> Unit
) {
    val hasContent = results.sections.isNotEmpty() || results.loading
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.WindowCornerRadius,
        onDismissRequest = onClose,
        visible = hasContent
    ) {
        if (!hasContent) return@BlurredWindow
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .spPanelSurface(RoundedCornerShape(OverlayMetrics.WindowCornerRadius), blurred = blurEnabled)
        ) {
            if (results.sections.isEmpty()) {
                ResultsSkeleton(blurEnabled)
            } else {
                SearchResultsPanel(results.sections, maxHeight, blurEnabled, icons, callbacks)
            }
        }
    }
}
