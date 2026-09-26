package com.paraskcd.spotlightsearch.designsystem.ds.atoms

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics

@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(DsMetrics.CornerSmall)
) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = DsMetrics.SkeletonMinAlpha,
        targetValue = DsMetrics.SkeletonMaxAlpha,
        animationSpec = infiniteRepeatable(tween(DsMetrics.SkeletonPulseMs), RepeatMode.Reverse),
        label = "skeletonAlpha"
    )
    Box(
        modifier = modifier
            .graphicsLayer { this.alpha = alpha }
            .clip(shape)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = DsMetrics.SkeletonFillAlpha))
    )
}
