package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun spGradients(ramp: BrandRamp, surfaceRamp: BrandRamp) = SpGradients(
    topGlow = Brush.verticalGradient(
        0.00f to surfaceRamp.s600,
        0.14f to surfaceRamp.s500,
        0.30f to surfaceRamp.s500.copy(alpha = 0.78f),
        0.50f to surfaceRamp.s500.copy(alpha = 0.46f),
        0.72f to surfaceRamp.s600.copy(alpha = 0.20f),
        1.00f to Color.Transparent
    ),
    buttonFill = Brush.linearGradient(listOf(ramp.s400, ramp.s700)),
    progress = Brush.horizontalGradient(listOf(ramp.s400, ramp.s500))
)
