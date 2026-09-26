package com.paraskcd.spotlightsearch.designsystem.signature.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpSegment(label: String, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = SpTheme.colors
    val background by animateColorAsState(
        targetValue = if (active) colors.brandTint else Color.Transparent,
        animationSpec = tween(SpMotion.durMorphMs, easing = SpMotion.easeIos),
        label = "segment"
    )
    Box(
        modifier = modifier
            .height(SpMetrics.segmentHeight)
            .clip(SpShapes.pill)
            .background(background)
            .clickableQuiet(onClick)
            .padding(horizontal = SpSpacing.s3),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = SpMetrics.segmentTextSize,
            fontWeight = FontWeight.Bold,
            color = if (active) colors.brandText else colors.textTertiary,
            maxLines = 1
        )
    }
}
