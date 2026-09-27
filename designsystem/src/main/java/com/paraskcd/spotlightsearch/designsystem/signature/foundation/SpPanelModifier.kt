package com.paraskcd.spotlightsearch.designsystem.signature.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpGlass
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun Modifier.spPanelSurface(shape: Shape, blurred: Boolean): Modifier {
    val colors = SpTheme.colors
    val alpha = if (blurred) LocalSpPanelAlpha.current else SpMetrics.panelAlphaSolid
    return this
        .clip(shape)
        .background(colors.bgBase.copy(alpha = alpha))
        .border(SpGlass.borderWidth, colors.glassBorder, shape)
        .border(
            width = SpGlass.specularWidth,
            brush = Brush.verticalGradient(
                0f to colors.glassSpecular,
                SpGlass.specularStop to Color.Transparent,
                1f to Color.Transparent
            ),
            shape = shape
        )
}
