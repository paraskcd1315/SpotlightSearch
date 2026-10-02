package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Stable
class OverlayDrag(
    private val scope: CoroutineScope,
    private val motion: OverlayMotion,
    private val travelPx: Float,
    private val flingVelocity: Float,
    private val onShown: (Float) -> Unit,
    private val onDismissed: () -> Unit
) {
    private var fingerY = 0f
    private var anchor: Float? = null
    private val tracker = VelocityTracker()
    private var ending: Job? = null

    val connection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (anchor == null || source != NestedScrollSource.UserInput) return Offset.Zero
            follow()
            return if (motion.moving) Offset(0f, available.y) else Offset.Zero
        }

        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            if (source != NestedScrollSource.UserInput || available.y <= 0f) return Offset.Zero
            follow()
            return Offset(0f, available.y)
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            if (anchor == null) return Velocity.Zero
            release()
            return available
        }
    }

    fun onFinger(timeMillis: Long, screenY: Float, down: Boolean) {
        if (down) tracker.resetTracking()
        fingerY = screenY
        tracker.addPosition(timeMillis, Offset(0f, screenY))
    }

    fun follow() {
        ending?.cancel()
        val start = anchor ?: (fingerY - motion.shiftPx).also { anchor = it }
        val shown = 1f - (fingerY - start).coerceIn(0f, travelPx) / travelPx
        motion.follow(shown, travelPx)
        onShown(shown)
    }

    fun release() {
        if (anchor == null) return
        anchor = null
        val velocityDown = tracker.calculateVelocity().y
        val closes = when {
            velocityDown >= flingVelocity -> true
            velocityDown <= -flingVelocity -> false
            else -> motion.shiftPx >= travelPx * OverlayMetrics.DismissFraction
        }
        ending = scope.launch {
            if (closes) {
                motion.leave(onShown)
                onDismissed()
            } else {
                motion.settle(onShown)
            }
        }
    }
}
