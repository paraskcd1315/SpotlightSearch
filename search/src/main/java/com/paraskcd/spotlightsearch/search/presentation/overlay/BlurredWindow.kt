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
import kotlinx.coroutines.android.awaitFrame
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
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
    animateIn: Boolean = false,
    heightPx: Int? = null,
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
        val density = LocalDensity.current
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val elevationPx = with(density) { OverlayMetrics.WindowElevation.toPx() }
        val blur = remember { Animatable(0f) }
        val reveal = remember { Animatable(if (animateIn) 0f else 1f) }
        var configured by remember { mutableStateOf(false) }
        var laidOut by remember { mutableStateOf(!animateIn) }
        val shown = configured && laidOut && (visible || animateIn)

        remember(focusable, cornerRadiusPx, offsetX, gravity, wrapWidth) {
            val width = if (wrapWidth) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                (DialogWindowSetup.displayWidth(window) * OverlayMetrics.WindowWidthFraction).toInt()
            }
            DialogWindowSetup.configure(
                window = window,
                focusable = focusable,
                widthPx = width,
                cornerRadiusPx = cornerRadiusPx,
                offsetYPx = offsetY,
                elevationPx = elevationPx,
                shadowAlpha = OverlayMetrics.WindowShadowAlpha,
                gravity = gravity,
                offsetXPx = offsetX
            )
            DialogWindowSetup.place(window, offsetY, alpha = 0f)
        }
        LaunchedEffect(Unit) {
            awaitFrame()
            awaitFrame()
            configured = true
        }
        LaunchedEffect(visible && configured, animateIn) {
            if (!animateIn) return@LaunchedEffect
            if (!(visible && configured)) {
                if (laidOut) reveal.animateTo(0f, tween(SpMotion.durMorphMs, easing = SpMotion.easeIos))
                laidOut = false
                reveal.snapTo(0f)
                return@LaunchedEffect
            }
            awaitFrame()
            awaitFrame()
            laidOut = true
            reveal.animateTo(1f, tween(SpMotion.durAutoHeightMs, easing = SpMotion.easeIos))
        }
        val progress = reveal.value
        SideEffect {
            DialogWindowSetup.place(
                window = window,
                offsetYPx = offsetY,
                alpha = if (configured) progress else 0f,
                heightPx = heightPx ?: ViewGroup.LayoutParams.WRAP_CONTENT
            )
            DialogWindowSetup.setVisible(window, shown || focusable)
        }
        LaunchedEffect(blurEnabled, shown) {
            if (!shown) {
                blur.snapTo(0f)
                DialogWindowSetup.setBlur(window, 0)
                return@LaunchedEffect
            }
            val target = if (blurEnabled) OverlayMetrics.BlurRadiusMax.toFloat() else 0f
            blur.animateTo(target, tween(OverlayMetrics.BlurRampMs)) {
                DialogWindowSetup.setBlur(window, value.toInt())
            }
        }
        content()
    }
}
