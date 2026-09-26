package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

private val RampSteps = listOf(16f, 8f, 0f, -8f, -15f, -22f)
private const val LightnessMin = 6f
private const val LightnessMax = 94f
private const val FullSaturation = 100f
const val SurfaceRampMaxSaturation = 55f

fun brandRampOf(color: Color, maxSaturation: Float = FullSaturation): BrandRamp {
    val (hue, rawSaturation, lightness) = rgbToHsl(color)
    val saturation = rawSaturation.coerceAtMost(maxSaturation)
    val shades = RampSteps.map { delta ->
        hslToColor(hue, saturation, (lightness + delta).coerceIn(LightnessMin, LightnessMax))
    }
    return BrandRamp(shades[0], shades[1], shades[2], shades[3], shades[4], shades[5])
}

private fun rgbToHsl(color: Color): Triple<Float, Float, Float> {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min
    val hue = when {
        delta == 0f -> 0f
        max == r -> 60f * (((g - b) / delta) % 6f)
        max == g -> 60f * ((b - r) / delta + 2f)
        else -> 60f * ((r - g) / delta + 4f)
    }
    val lightness = (max + min) / 2f
    val saturation = if (delta == 0f) 0f else delta / (1f - abs(2f * lightness - 1f))
    return Triple(if (hue < 0f) hue + 360f else hue, saturation * 100f, lightness * 100f)
}

private fun hslToColor(hue: Float, saturation: Float, lightness: Float): Color {
    val s = saturation / 100f
    val l = lightness / 100f
    val c = (1f - abs(2f * l - 1f)) * s
    val x = c * (1f - abs(((hue / 60f) % 2f) - 1f))
    val m = l - c / 2f
    val (r, g, b) = when {
        hue < 60f -> Triple(c, x, 0f)
        hue < 120f -> Triple(x, c, 0f)
        hue < 180f -> Triple(0f, c, x)
        hue < 240f -> Triple(0f, x, c)
        hue < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    return Color(red = (r + m).coerceIn(0f, 1f), green = (g + m).coerceIn(0f, 1f), blue = (b + m).coerceIn(0f, 1f))
}
