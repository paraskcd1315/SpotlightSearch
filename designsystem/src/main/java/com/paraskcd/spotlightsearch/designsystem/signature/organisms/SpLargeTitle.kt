package com.paraskcd.spotlightsearch.designsystem.signature.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing

@Composable
fun SpLargeTitle(title: String, fraction: Float, onMeasured: (Int) -> Unit) {
    var fullPx by remember { mutableIntStateOf(0) }
    val fullHeight = with(LocalDensity.current) { fullPx.toDp() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(fullHeight * (1f - fraction))
            .clipToBounds()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            maxLines = SpMetrics.largeTitleMaxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top, unbounded = true)
                .padding(start = SpSpacing.s5, end = SpSpacing.s5, bottom = SpSpacing.s3)
                .onSizeChanged {
                    if (it.height != fullPx) {
                        fullPx = it.height
                        onMeasured(it.height)
                    }
                }
                .graphicsLayer {
                    alpha = 1f - fraction
                    translationY = -fullPx * fraction * SpMetrics.largeTitleLift
                }
        )
    }
}
