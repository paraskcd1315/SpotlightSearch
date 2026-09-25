package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
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
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.WindowCornerRadius,
        onDismissRequest = onClose
    ) {
        if (results.sections.isEmpty() && !results.loading) return@BlurredWindow
        val shape = RoundedCornerShape(OverlayMetrics.WindowCornerRadius)
        val alpha = if (blurEnabled) OverlayMetrics.PanelAlphaWithBlur else OverlayMetrics.PanelAlphaWithoutBlur
        Surface(
            shape = shape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
            modifier = Modifier
                .fillMaxWidth()
                .border(DsMetrics.OutlineWidth, MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha), shape)
        ) {
            if (results.sections.isEmpty()) {
                ResultsSkeleton(blurEnabled)
            } else {
                SearchResultsPanel(results.sections, maxHeight, blurEnabled, icons, callbacks)
            }
        }
    }
}
