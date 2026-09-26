package com.paraskcd.spotlightsearch.designsystem.ds.foundation

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

fun Modifier.fadingEdges(
    scrollableState: ScrollableState,
    topEdgeHeight: Dp = DsMetrics.FadeEdgeHeight,
    bottomEdgeHeight: Dp = DsMetrics.FadeEdgeHeight
) = then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val topHeight = topEdgeHeight.toPx()
            val bottomHeight = bottomEdgeHeight.toPx()
            val hasScrollableContent =
                scrollableState.canScrollBackward || scrollableState.canScrollForward
            if (!hasScrollableContent) return@drawWithContent

            if (topHeight >= 1f) {
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = topHeight
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
            if (bottomHeight >= 1f) {
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(Color.Black, Color.Transparent),
                        startY = size.height - bottomHeight,
                        endY = size.height
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
        }
)
