package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spPanelSurface
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.components.ResultsFilter
import com.paraskcd.spotlightsearch.search.presentation.components.ResultsSkeleton
import com.paraskcd.spotlightsearch.search.presentation.components.SearchResultsPanel
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.model.SearchResults
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

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
    var filter by remember { mutableStateOf<SectionKind?>(null) }
    LaunchedEffect(hasContent) { if (!hasContent) filter = null }
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
                return@Column
            }
            val kinds = results.sections.map { it.kind }
            val active = filter?.takeIf { it in kinds }
            val showFilter = kinds.size > 1
            if (showFilter) ResultsFilter(kinds, active) { filter = it }
            SearchResultsPanel(
                sections = if (active == null) results.sections else results.sections.filter { it.kind == active },
                maxHeight = if (showFilter) maxHeight - SearchMetrics.FilterReserve else maxHeight,
                blurEnabled = blurEnabled,
                icons = icons,
                callbacks = callbacks
            )
        }
    }
}
