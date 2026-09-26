package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spPanelSurface
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.presentation.components.ResultsSkeleton
import com.paraskcd.spotlightsearch.search.presentation.components.SearchResultsPanel
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.model.SearchResults

@Composable
fun ResultsWindow(
    results: SearchResults,
    offsetY: Int,
    maxHeightPx: Int,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    scrollKey: Any?,
    onShowAll: (SearchSection) -> Unit,
    onClose: () -> Unit
) {
    val hasContent = results.sections.isNotEmpty() || results.loading
    var retained by remember { mutableStateOf(results) }
    var contentPx by remember { mutableIntStateOf(0) }
    SideEffect { if (hasContent) retained = results }
    val shown = if (hasContent) results else retained
    val maxHeight = with(LocalDensity.current) { maxHeightPx.toDp() }
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.WindowCornerRadius,
        onDismissRequest = onClose,
        visible = hasContent,
        animateIn = true,
        heightPx = contentPx.takeIf { it > 0 }?.coerceAtMost(maxHeightPx)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(max = maxHeight)
                    .onSizeChanged { contentPx = it.height }
                    .spPanelSurface(RoundedCornerShape(OverlayMetrics.WindowCornerRadius), blurred = blurEnabled)
            ) {
                if (shown.sections.isEmpty()) {
                    ResultsSkeleton(blurEnabled)
                } else {
                    SearchResultsPanel(shown.sections, shown.loading, blurEnabled, icons, callbacks, scrollKey, onShowAll)
                }
            }
        }
    }
}
