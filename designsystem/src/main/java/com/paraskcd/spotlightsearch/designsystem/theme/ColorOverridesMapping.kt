package com.paraskcd.spotlightsearch.designsystem.theme

import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpColors

private const val SecondaryTextAlpha = 0.58f
private const val TertiaryTextAlpha = 0.40f
private const val BorderAlpha = 0.14f
private const val GlassBorderAlpha = 0.12f

fun ColorOverrides.applyTo(colors: SpColors): SpColors {
    var result = colors
    background?.let { result = result.copy(bgBase = it) }
    surfaceBright?.let { result = result.copy(surfaceContainerHigh = it, glassStrongBg = it) }
    onSurface?.let {
        result = result.copy(
            textPrimary = it,
            textSecondary = it.copy(alpha = SecondaryTextAlpha),
            textTertiary = it.copy(alpha = TertiaryTextAlpha)
        )
    }
    outline?.let { result = result.copy(border = it.copy(alpha = BorderAlpha), glassBorder = it.copy(alpha = GlassBorderAlpha)) }
    return result
}
