package com.paraskcd.spotlightsearch.preferences.presentation.utils

import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.paraskcd.spotlightsearch.preferences.presentation.model.Hsv

object HsvColor {
    val HueStops = listOf(0f, 60f, 120f, 180f, 240f, 300f, 360f)
    private val HexDigits = "0123456789ABCDEFabcdef"
    private const val OPAQUE = 0xFF000000L
    private const val RGB_MASK = 0xFFFFFF
    private const val HEX_LENGTH = 6

    fun toHsv(color: Color): Hsv {
        val values = FloatArray(3)
        AndroidColor.colorToHSV(color.toArgb(), values)
        return Hsv(values[0], values[1], values[2])
    }

    fun fromHsv(hue: Float, saturation: Float, brightness: Float): Color =
        Color(AndroidColor.HSVToColor(floatArrayOf(hue, saturation, brightness)))

    fun hex(color: Color): String = "#%06X".format(color.toArgb() and RGB_MASK)

    fun parseHex(text: String): Color? {
        val clean = text.removePrefix("#")
        if (clean.length != HEX_LENGTH || !clean.all { it in HexDigits }) return null
        return Color((OPAQUE or clean.toLong(16)).toInt())
    }
}
