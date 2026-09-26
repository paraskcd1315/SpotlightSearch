package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale

internal object ThemedIconRenderer {
    private const val SIZE = 128
    private const val PLAIN_ICON_SCALE = 0.65f
    private const val CIRCLE_SCALE = 1f
    private const val GRADIENT_INNER_ALPHA = 0x30000000
    private const val GRADIENT_OUTER_ALPHA = 0x60000000

    fun render(icon: Drawable, tint: Int): Bitmap {
        val monochrome = (icon as? AdaptiveIconDrawable)?.monochrome
        return if (monochrome != null) monochromeOnCircle(monochrome, tint) else colorizedOnCircle(icon, tint)
    }

    private fun monochromeOnCircle(monochrome: Drawable, tint: Int): Bitmap {
        val (bitmap, canvas) = circle(tint)
        val tinted = monochrome.mutate().apply { colorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN) }
        canvas.drawBitmap(tinted.toBitmap(SIZE, SIZE), 0f, 0f, null)
        return bitmap
    }

    private fun colorizedOnCircle(icon: Drawable, tint: Int): Bitmap {
        val (bitmap, canvas) = circle(tint)
        val iconSize = (SIZE * PLAIN_ICON_SCALE).toInt()
        val offset = (SIZE - iconSize) / 2f
        val red = Color.red(tint) / 255f
        val green = Color.green(tint) / 255f
        val blue = Color.blue(tint) / 255f
        val colorize = ColorMatrix(
            floatArrayOf(
                0.299f * red, 0.587f * red, 0.114f * red, 0f, 0f,
                0.299f * green, 0.587f * green, 0.114f * green, 0f, 0f,
                0.299f * blue, 0.587f * blue, 0.114f * blue, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorize)
            isAntiAlias = true
        }
        canvas.drawBitmap(icon.toBitmap().scale(iconSize, iconSize), offset, offset, paint)
        return bitmap
    }

    private fun circle(tint: Int): Pair<Bitmap, Canvas> {
        val bitmap = createBitmap(SIZE, SIZE)
        val canvas = Canvas(bitmap)
        val radius = SIZE / 2f
        val paint = Paint().apply {
            isAntiAlias = true
            shader = RadialGradient(
                radius, radius, radius,
                intArrayOf(tint and 0x00FFFFFF or GRADIENT_INNER_ALPHA, tint and 0x00FFFFFF or GRADIENT_OUTER_ALPHA),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(radius, radius, radius * CIRCLE_SCALE, paint)
        return bitmap to canvas
    }
}
