package com.paraskcd.spotlightsearch

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenshotProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var cachedScreenshot: Bitmap? = null

    init {
        cachedScreenshot = capture()
    }

    private fun capture(): Bitmap? {
        val window = (context as? Activity)?.window ?: return null
        val view = window.decorView.rootView
        val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        view.draw(canvas)
        return screenshot
    }

    fun takeScreenshot(): Bitmap? = cachedScreenshot
}