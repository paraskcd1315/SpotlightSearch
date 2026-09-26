package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import kotlin.math.roundToInt

class KeyboardTracker(
    private val view: View,
    private val onTopOnScreen: (Int) -> Unit,
    private val onKeyboardShown: () -> Unit
) : WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
    private var hiddenTop: Int? = null
    private var shownTop: Int? = null
    private var showing = false
    private var animating = false
    private var pendingRest: Runnable? = null

    fun onRest() {
        if (animating) return
        pendingRest?.let(view::removeCallbacks)
        val report = Runnable {
            pendingRest = null
            if (animating) return@Runnable
            val top = topOnScreen()
            val hidden = hiddenTop
            if (hidden == null || top > hidden) hiddenTop = top
            if (top < (hiddenTop ?: top)) shownTop = top
            onTopOnScreen(top)
        }
        pendingRest = report
        view.postOnAnimation(report)
    }

    override fun onPrepare(animation: WindowInsetsAnimation) {
        if (animation.typeMask and WindowInsets.Type.ime() == 0) return
        animating = true
        pendingRest?.let(view::removeCallbacks)
        pendingRest = null
    }

    override fun onStart(animation: WindowInsetsAnimation, bounds: WindowInsetsAnimation.Bounds): WindowInsetsAnimation.Bounds {
        if (animation.typeMask and WindowInsets.Type.ime() == 0) return bounds
        showing = view.rootWindowInsets?.isVisible(WindowInsets.Type.ime()) == true
        if (showing) shownTop = topOnScreen()
        return bounds
    }

    override fun onProgress(insets: WindowInsets, running: MutableList<WindowInsetsAnimation>): WindowInsets {
        val ime = running.firstOrNull { it.typeMask and WindowInsets.Type.ime() != 0 } ?: return insets
        val hidden = hiddenTop ?: return insets
        val shown = shownTop ?: return insets
        val fraction = if (showing) ime.interpolatedFraction else 1f - ime.interpolatedFraction
        onTopOnScreen(hidden - (fraction * (hidden - shown)).roundToInt())
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimation) {
        if (animation.typeMask and WindowInsets.Type.ime() == 0) return
        animating = false
        val top = if (showing) shownTop else hiddenTop
        top?.let(onTopOnScreen)
        if (showing) onKeyboardShown()
    }

    private fun topOnScreen(): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[1]
    }
}
