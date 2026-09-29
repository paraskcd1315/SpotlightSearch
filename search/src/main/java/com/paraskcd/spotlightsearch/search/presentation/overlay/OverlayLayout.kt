package com.paraskcd.spotlightsearch.search.presentation.overlay

import kotlin.math.roundToInt

data class OverlayLayout(
    val panelWidthPx: Int?,
    val settingsOffsetY: Int,
    val filterOffsetY: Int,
    val filterMaxWidthPx: Int,
    val panelOffsetY: Int,
    val panelCapPx: Int
) {
    companion object {
        fun barWidthPx(landscape: Boolean, displayWidthPx: Int): Int? =
            if (landscape) (displayWidthPx * OverlayMetrics.LandscapeBarWidthFraction).roundToInt() else null

        fun chromeAbovePanelPx(landscape: Boolean, toolbarHeightPx: Int): Int =
            if (landscape) 0 else toolbarHeightPx

        fun of(
            landscape: Boolean,
            displayWidthPx: Int,
            displayHeightPx: Int,
            barTopPx: Int,
            barHeightPx: Int,
            toolbarHeightPx: Int,
            settingsWidthPx: Int,
            filterHeightPx: Int,
            sideMarginPx: Int,
            gapPx: Int,
            ceilingPx: Int
        ): OverlayLayout {
            val aboveBar = displayHeightPx - barTopPx + gapPx
            val underPanel = if (landscape) 0 else chromeAbovePanelPx(landscape, toolbarHeightPx) + gapPx
            val panelOffsetY = aboveBar + underPanel
            val panelCapPx = (barTopPx - gapPx - underPanel - ceilingPx).coerceAtLeast(0)
            val barWidth = barWidthPx(landscape, displayWidthPx)
            if (barWidth != null) {
                val rowCenter = displayHeightPx - barTopPx - barHeightPx / 2
                return OverlayLayout(
                    panelWidthPx = (displayWidthPx * OverlayMetrics.LandscapePanelWidthFraction).roundToInt(),
                    settingsOffsetY = rowCenter - toolbarHeightPx / 2,
                    filterOffsetY = rowCenter - filterHeightPx / 2,
                    filterMaxWidthPx = (displayWidthPx - barWidth) / 2 - sideMarginPx - gapPx,
                    panelOffsetY = panelOffsetY,
                    panelCapPx = panelCapPx
                )
            }
            return OverlayLayout(
                panelWidthPx = null,
                settingsOffsetY = aboveBar,
                filterOffsetY = aboveBar + ((toolbarHeightPx - filterHeightPx) / 2).coerceAtLeast(0),
                filterMaxWidthPx = displayWidthPx - 2 * sideMarginPx - settingsWidthPx - gapPx,
                panelOffsetY = panelOffsetY,
                panelCapPx = panelCapPx
            )
        }
    }
}
