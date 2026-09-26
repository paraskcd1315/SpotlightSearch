package com.paraskcd.spotlightsearch.search.infrastructure.window

import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.WindowManager

object DialogWindowSetup {
    fun configure(
        window: Window,
        focusable: Boolean,
        widthPx: Int,
        cornerRadiusPx: Float,
        offsetYPx: Int,
        elevationPx: Float,
        shadowAlpha: Float,
        gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
        offsetXPx: Int = 0
    ) {
        window.decorView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadiusPx)
                outline.alpha = shadowAlpha
            }
        }
        window.setElevation(elevationPx)
        window.setBackgroundDrawable(
            GradientDrawable().apply {
                cornerRadius = cornerRadiusPx
                setColor(Color.TRANSPARENT)
            }
        )
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0f)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        if (focusable) {
            window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        window.setGravity(gravity)
        window.setLayout(widthPx, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.attributes = window.attributes.apply {
            x = offsetXPx
            y = offsetYPx
        }
    }

    fun place(window: Window, offsetYPx: Int, alpha: Float) {
        val attributes = window.attributes
        if (attributes.y == offsetYPx && attributes.alpha == alpha) return
        window.attributes = attributes.apply {
            y = offsetYPx
            this.alpha = alpha
        }
    }

    fun setVisible(window: Window, visible: Boolean) {
        window.decorView.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    fun setBlur(window: Window, radius: Int) {
        window.setBackgroundBlurRadius(radius)
    }

    fun displayHeight(window: Window): Int =
        window.windowManager.currentWindowMetrics.bounds.height()

    fun displayWidth(window: Window): Int =
        window.windowManager.currentWindowMetrics.bounds.width()
}
