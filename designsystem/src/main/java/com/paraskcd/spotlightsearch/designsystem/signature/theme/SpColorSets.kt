package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.ui.graphics.Color

fun spDarkColors(ramp: BrandRamp) = SpColors(
    isLight = false,
    brand = ramp.s500,
    brandText = ramp.s300,
    brandTint = ramp.s500.copy(alpha = 0.22f),
    bgBase = SpDarkBgBase,
    surface = SpDarkSurface,
    surfaceContainer = SpDarkSurfaceContainer,
    surfaceContainerHigh = SpDarkSurfaceContainerHigh,
    textPrimary = Color.White.copy(alpha = 0.92f),
    textSecondary = Color.White.copy(alpha = 0.58f),
    textTertiary = Color.White.copy(alpha = 0.40f),
    glassBg = Color.White.copy(alpha = 0.06f),
    glassStrongBg = SpDarkSurfaceContainerHigh.copy(alpha = 0.80f),
    glassBorder = Color.White.copy(alpha = 0.12f),
    glassSpecular = Color.White.copy(alpha = 0.16f),
    glassBlurTintAlpha = 0.62f,
    border = Color.White.copy(alpha = 0.14f),
    hairline = Color.White.copy(alpha = 0.10f),
    scrim = Color.Black.copy(alpha = 0.52f),
    danger = SpDanger,
    dangerText = SpDangerTextDark
)

fun spLightColors(ramp: BrandRamp) = SpColors(
    isLight = true,
    brand = ramp.s500,
    brandText = ramp.s600,
    brandTint = ramp.s500.copy(alpha = 0.14f),
    bgBase = SpLightBgBase,
    surface = SpLightSurface,
    surfaceContainer = SpLightSurfaceContainer,
    surfaceContainerHigh = SpLightSurfaceContainerHigh,
    textPrimary = SpInkLight.copy(alpha = 0.92f),
    textSecondary = SpInkLight.copy(alpha = 0.56f),
    textTertiary = SpInkLight.copy(alpha = 0.40f),
    glassBg = Color.White.copy(alpha = 0.55f),
    glassStrongBg = Color.White.copy(alpha = 0.65f),
    glassBorder = SpBorderLight.copy(alpha = 0.12f),
    glassSpecular = Color.White.copy(alpha = 0.85f),
    glassBlurTintAlpha = 0.55f,
    border = SpBorderLight.copy(alpha = 0.14f),
    hairline = Color.Black.copy(alpha = 0.08f),
    scrim = Color.Black.copy(alpha = 0.40f),
    danger = SpDanger,
    dangerText = SpDangerTextLight
)
