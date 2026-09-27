package com.paraskcd.spotlightsearch.search.infrastructure.icons

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
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
import kotlin.math.abs
import kotlin.math.roundToInt

internal object ThemedIconRenderer {
    private const val SIZE = 128
    private const val PLAIN_ICON_SCALE = 0.65f
    private const val GRADIENT_INNER_ALPHA = 0x30000000
    private const val GRADIENT_OUTER_ALPHA = 0x60000000
    private const val RGB_MASK = 0x00FFFFFF
    private const val OPAQUE_ALPHA = 200
    private const val VISIBLE_ALPHA = 24
    private const val CONTRAST_GAIN = 3f
    private const val CHANNEL_MAX = 255
    private const val DOMINANT_SHARE = 0.3f
    private const val QUANTIZE_MASK = 0x00F0F0F0
    private const val LOGO_COVERAGE = 0.01f
    private const val LOGO_MAX_SHARE = 0.6f
    private const val MIN_CONTRAST = 96
    private const val LUMINANCE_THRESHOLD = 0.3f
    private const val LUMINANCE_SOFT = 0.15f
    private const val RED_WEIGHT = 0.299f
    private const val GREEN_WEIGHT = 0.587f
    private const val BLUE_WEIGHT = 0.114f
    private const val RING_FRACTION = 0.02f
    private const val RING_COLOR = 0x1FFFFFFF

    fun render(icon: Drawable, tint: Int, background: Int?): Bitmap {
        val adaptive = icon as? AdaptiveIconDrawable
        val layer = adaptive?.monochrome ?: adaptive?.foreground
        val plainSize = (SIZE * PLAIN_ICON_SCALE).roundToInt()
        if (layer == null) {
            val source = icon.toBitmap().scale(plainSize, plainSize)
            return onCircle(silhouette(source) ?: source, tint, background, plainSize)
        }
        val source = layer.mutate().toBitmap(SIZE, SIZE)
        val shape = silhouette(source)
        if (shape != null || adaptive?.monochrome != null) return onCircle(shape ?: source, tint, background, SIZE)
        val composite = silhouette(icon.toBitmap(plainSize, plainSize))
        return if (composite != null) onCircle(composite, tint, background, plainSize) else onCircle(source, tint, background, SIZE)
    }

    private fun onCircle(shape: Bitmap, tint: Int, background: Int?, shapeSize: Int): Bitmap {
        val bitmap = createBitmap(SIZE, SIZE)
        val canvas = Canvas(bitmap)
        val radius = SIZE / 2f
        if (background != null) {
            canvas.drawCircle(radius, radius, radius, Paint().apply {
                isAntiAlias = true
                color = background
            })
            val ring = SIZE * RING_FRACTION
            canvas.drawCircle(radius, radius, radius - ring / 2f, Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = ring
                color = RING_COLOR
            })
        } else {
            canvas.drawCircle(radius, radius, radius, Paint().apply {
                isAntiAlias = true
                shader = RadialGradient(
                    radius, radius, radius,
                    intArrayOf(tint and RGB_MASK or GRADIENT_INNER_ALPHA, tint and RGB_MASK or GRADIENT_OUTER_ALPHA),
                    floatArrayOf(0f, 1f),
                    Shader.TileMode.CLAMP
                )
            })
        }
        val offset = (SIZE - shapeSize) / 2f
        val tinted = Paint().apply {
            isAntiAlias = true
            colorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
        }
        canvas.drawBitmap(shape, offset, offset, tinted)
        return bitmap
    }

    private fun silhouette(source: Bitmap): Bitmap? {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        val opaque = pixels.filter { Color.alpha(it) >= OPAQUE_ALPHA }
        if (opaque.isEmpty()) return source
        val (base, baseCount) = opaque.groupingBy { it and QUANTIZE_MASK }.eachCount().maxBy { it.value }
        if (baseCount < opaque.size * DOMINANT_SHARE) return byLuminance(source, pixels, opaque)
        val contrasting = opaque.count { distance(it, base) >= MIN_CONTRAST }
        if (contrasting < opaque.size * LOGO_COVERAGE) return source
        var visible = 0
        for (index in pixels.indices) {
            val pixel = pixels[index]
            val alpha = (distance(pixel, base) * CONTRAST_GAIN).roundToInt().coerceAtMost(CHANNEL_MAX) * Color.alpha(pixel) / CHANNEL_MAX
            if (alpha >= VISIBLE_ALPHA) visible++
            pixels[index] = if (alpha < VISIBLE_ALPHA) Color.TRANSPARENT else Color.argb(alpha, CHANNEL_MAX, CHANNEL_MAX, CHANNEL_MAX)
        }
        if (visible < pixels.size * LOGO_COVERAGE) return null
        return createBitmap(width, height).apply { setPixels(pixels, 0, width, 0, 0, width, height) }
    }

    private fun byLuminance(source: Bitmap, pixels: IntArray, opaque: List<Int>): Bitmap? {
        val median = opaque.map(::luminance).sorted()[opaque.size / 2]
        val contrasting = opaque.count { abs(luminance(it) - median) >= LUMINANCE_THRESHOLD }
        if (contrasting < opaque.size * LOGO_COVERAGE || contrasting > opaque.size * LOGO_MAX_SHARE) return source
        var visible = 0
        for (index in pixels.indices) {
            val pixel = pixels[index]
            val strength = ((abs(luminance(pixel) - median) - LUMINANCE_SOFT) / (LUMINANCE_THRESHOLD - LUMINANCE_SOFT)).coerceIn(0f, 1f)
            val alpha = (strength * Color.alpha(pixel)).roundToInt()
            if (alpha >= VISIBLE_ALPHA) visible++
            pixels[index] = if (alpha < VISIBLE_ALPHA) Color.TRANSPARENT else Color.argb(alpha, CHANNEL_MAX, CHANNEL_MAX, CHANNEL_MAX)
        }
        if (visible < pixels.size * LOGO_COVERAGE) return null
        return createBitmap(source.width, source.height).apply { setPixels(pixels, 0, source.width, 0, 0, source.width, source.height) }
    }

    private fun distance(pixel: Int, base: Int): Int =
        abs(Color.red(pixel) - Color.red(base)) + abs(Color.green(pixel) - Color.green(base)) + abs(Color.blue(pixel) - Color.blue(base))

    private fun luminance(pixel: Int): Float =
        (RED_WEIGHT * Color.red(pixel) + GREEN_WEIGHT * Color.green(pixel) + BLUE_WEIGHT * Color.blue(pixel)) / CHANNEL_MAX
}
