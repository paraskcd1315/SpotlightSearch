package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.abs

internal object IconPackRenderer {
    fun render(icon: Drawable, decoration: IconPackDecoration, seed: Int, size: Int): Bitmap {
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        val back = decoration.backs[abs(seed % decoration.backs.size)]
        canvas.drawBitmap(back.toBitmap(size, size), 0f, 0f, null)
        val iconSize = (size * decoration.scale).toInt().coerceIn(1, size)
        val offset = (size - iconSize) / 2f
        val layer = createBitmap(size, size)
        Canvas(layer).apply {
            drawBitmap(icon.toBitmap(iconSize, iconSize), offset, offset, null)
            decoration.mask?.let {
                drawBitmap(it.toBitmap(size, size), 0f, 0f, Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT) })
            }
        }
        canvas.drawBitmap(layer, 0f, 0f, null)
        decoration.upon?.let { canvas.drawBitmap(it.toBitmap(size, size), 0f, 0f, null) }
        return bitmap
    }
}
