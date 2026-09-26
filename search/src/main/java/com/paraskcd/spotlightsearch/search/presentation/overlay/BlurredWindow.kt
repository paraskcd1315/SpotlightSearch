package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.Gravity
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.paraskcd.spotlightsearch.search.infrastructure.window.DialogWindowSetup

@Composable
fun BlurredWindow(
    focusable: Boolean,
    blurEnabled: Boolean,
    offsetY: Int,
    cornerRadius: Dp,
    onDismissRequest: () -> Unit,
    visible: Boolean = true,
    gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
    offsetX: Int = 0,
    wrapWidth: Boolean = false,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = focusable,
            dismissOnClickOutside = false
        )
    ) {
        val window = (LocalView.current.parent as DialogWindowProvider).window
        val cornerRadiusPx = with(LocalDensity.current) { cornerRadius.toPx() }
        val blur = remember { Animatable(0f) }
        var configured by remember { mutableStateOf(focusable) }

        LaunchedEffect(focusable, cornerRadiusPx, offsetY, offsetX, gravity, wrapWidth) {
            val width = if (wrapWidth) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                (DialogWindowSetup.displayWidth(window) * OverlayMetrics.WindowWidthFraction).toInt()
            }
            DialogWindowSetup.configure(window, focusable, width, cornerRadiusPx, offsetY, gravity, offsetX)
            configured = true
        }
        SideEffect { DialogWindowSetup.setVisible(window, visible && configured) }
        LaunchedEffect(blurEnabled, configured) {
            if (!configured) return@LaunchedEffect
            val target = if (blurEnabled) OverlayMetrics.BlurRadiusMax.toFloat() else 0f
            blur.animateTo(target, tween(OverlayMetrics.BlurRampMs)) {
                DialogWindowSetup.setBlur(window, value.toInt())
            }
        }
        content()
    }
}
