package com.paraskcd.spotlightsearch.designsystem.signature.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpColors
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpGlass
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.HazeColorEffect
import dev.chrisbanes.haze.blur.hazeBlur

fun Modifier.spHazeBlur(
    state: HazeState,
    colors: SpColors,
    alpha: Float = 1f,
    blurRadius: Dp = SpGlass.blurRadius
): Modifier = hazeBlur(
    input = HazeInput.Backdrop(state),
    style = HazeBlurStyle {
        backgroundColor(colors.bgBase)
        colorEffects(listOf(HazeColorEffect.tint(colors.surfaceContainerHigh.copy(alpha = colors.glassBlurTintAlpha))))
        blurRadius(blurRadius)
        noiseFactor(SpGlass.noiseFactor)
        alpha(alpha)
    }
)

@Composable
fun Modifier.spGlassSurface(
    shape: Shape,
    hazeState: HazeState? = null,
    specular: Boolean = true,
    strong: Boolean = false,
    panel: Boolean = false
): Modifier {
    val colors = SpTheme.colors
    val fill = when {
        panel -> colors.bgBase.copy(alpha = SpMetrics.panelAlphaBlurred)
        strong -> colors.glassStrongBg
        else -> colors.glassBg
    }
    return this
        .clip(shape)
        .then(if (hazeState != null) Modifier.spHazeBlur(hazeState, colors) else Modifier.background(fill))
        .border(SpGlass.borderWidth, colors.glassBorder, shape)
        .then(
            if (specular) {
                Modifier.border(
                    width = SpGlass.specularWidth,
                    brush = Brush.verticalGradient(
                        0f to colors.glassSpecular,
                        SpGlass.specularStop to Color.Transparent,
                        1f to Color.Transparent
                    ),
                    shape = shape
                )
            } else {
                Modifier
            }
        )
}
