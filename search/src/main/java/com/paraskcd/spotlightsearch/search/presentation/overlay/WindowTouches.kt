package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.MotionEvent
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun WindowTouches(window: Window, onTouch: (MotionEvent) -> Unit) {
    val touch by rememberUpdatedState(onTouch)
    DisposableEffect(window) {
        val original = window.callback
        window.callback = object : Window.Callback by original {
            override fun dispatchTouchEvent(event: MotionEvent): Boolean {
                touch(event)
                return original.dispatchTouchEvent(event)
            }
        }
        onDispose { window.callback = original }
    }
}
