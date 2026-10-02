package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.android.awaitFrame
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpTextScaled
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
    heightPx: Int? = null,
    keyboardAtStart: Boolean = true,
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
        val motion = LocalOverlayMotion.current
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val elevationPx = with(density) { OverlayMetrics.WindowElevation.toPx() }
        val blur = remember { Animatable(0f) }
        val reveal = remember { Animatable(if (animateIn) 0f else 1f) }
        var configured by remember { mutableStateOf(false) }
        var laidOut by remember { mutableStateOf(!animateIn) }
        var measuredHeightPx by remember { mutableStateOf<Int?>(null) }
        val shown = configured && laidOut && (visible || animateIn)
        val measuresOwnHeight = !focusable && heightPx == null

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
                offsetXPx = offsetX,
                keyboardAtStart = keyboardAtStart
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
        val restY = rememberUpdatedState(offsetY)
        val windowHeightPx = rememberUpdatedState(heightPx ?: measuredHeightPx ?: ViewGroup.LayoutParams.WRAP_CONTENT)
        val placement = {
            val shiftPx = motion.shiftPx.roundToInt()
            WindowPlacement(
                y = restY.value - shiftPx,
                alpha = if (configured) reveal.value * motion.alpha else 0f,
                heightPx = windowHeightPx.value,
                animatedMoves = shiftPx == 0
            )
        }
        val place = { placement: WindowPlacement ->
            DialogWindowSetup.place(window, placement.y, placement.alpha, placement.heightPx, placement.animatedMoves)
        }
        SideEffect {
            place(placement())
            DialogWindowSetup.setVisible(window, shown || focusable)
        }
        LaunchedEffect(window) {
            snapshotFlow(placement).collect { place(it) }
        }
        DisposableEffect(window, motion) {
            val remove = motion.addMover { place(placement()) }
            onDispose(remove)
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
        val drag = LocalOverlayDrag.current
        if (drag != null) {
            WindowTouches(window) { event ->
                drag.onFinger(event.eventTime, event.rawY, down = event.actionMasked == MotionEvent.ACTION_DOWN)
            }
        }
        val ownHeight = if (measuresOwnHeight) {
            Modifier
                .wrapContentHeight(Alignment.Top, unbounded = true)
                .onSizeChanged { size -> measuredHeightPx = size.height.takeIf { it > 0 } }
        } else {
            Modifier
        }
        SpTextScaled {
            Box(modifier = ownHeight.overlayDismissDrag(drag), propagateMinConstraints = true) {
                content()
            }
        }
    }
}
