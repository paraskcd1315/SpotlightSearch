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
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.android.awaitFrame
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.search.infrastructure.window.DialogWindowSetup
import kotlin.math.roundToInt

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
        val risePx = with(density) { OverlayMetrics.RevealRise.toPx() }
        val blur = remember { Animatable(0f) }
        val reveal = remember { Animatable(if (animateIn) 0f else 1f) }
        var configured by remember { mutableStateOf(focusable) }
        var laidOut by remember { mutableStateOf(!animateIn) }
        val shown = visible && configured && laidOut

        val latestOffsetY by rememberUpdatedState(offsetY)
        LaunchedEffect(focusable, cornerRadiusPx, offsetX, gravity, wrapWidth) {
            val width = if (wrapWidth) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                (DialogWindowSetup.displayWidth(window) * OverlayMetrics.WindowWidthFraction).toInt()
            }
            DialogWindowSetup.configure(window, focusable, width, cornerRadiusPx, latestOffsetY, gravity, offsetX)
            awaitFrame()
            awaitFrame()
            configured = true
        }
        LaunchedEffect(visible && configured, animateIn) {
            if (!animateIn) return@LaunchedEffect
            if (!(visible && configured)) {
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
            if (configured) {
                DialogWindowSetup.place(window, offsetY - (risePx * (1f - progress)).roundToInt(), progress)
            }
            DialogWindowSetup.setVisible(window, shown)
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
