package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import kotlin.coroutines.cancellation.CancellationException

@Stable
class OverlayMotion(initiallyShown: Float = 1f) {
    private var shown by mutableFloatStateOf(initiallyShown)
    private var distancePx by mutableFloatStateOf(0f)
    private var run = 0
    private val movers = mutableListOf<() -> Unit>()

    var anchored by mutableStateOf(false)

    var leaving by mutableStateOf(false)
        private set

    val shiftPx: Float
        get() = if (anchored) (1f - shown) * distancePx else 0f

    val alpha: Float
        get() = when {
            distancePx <= 0f -> shown
            !anchored -> 0f
            else -> (shown * OverlayMetrics.PeekAlphaLead).coerceAtMost(1f)
        }

    val scrimAlpha: Float
        get() = if (distancePx > 0f && !anchored) 0f else shown

    val moving: Boolean by derivedStateOf { shiftPx != 0f }

    fun addMover(mover: () -> Unit): () -> Unit {
        movers += mover
        return { movers -= mover }
    }

    fun rest() {
        run++
        shown = 1f
        moved()
    }

    fun follow(progress: Float, distancePx: Float) {
        run++
        this.distancePx = distancePx
        shown = progress.coerceIn(0f, 1f)
        moved()
    }

    suspend fun settle(onFrame: (Float) -> Unit = {}) {
        glide(1f, onFrame)
    }

    suspend fun leave(onFrame: (Float) -> Unit = {}) {
        leaving = true
        glide(0f, onFrame)
    }

    private suspend fun glide(target: Float, onFrame: (Float) -> Unit) {
        val mine = ++run
        animate(shown, target, animationSpec = tween(SpMotion.durMorphMs, easing = SpMotion.easeIos)) { value, _ ->
            if (run != mine) throw CancellationException()
            shown = value
            onFrame(value)
            moved()
        }
    }

    private fun moved() {
        for (index in movers.indices.reversed()) movers[index]()
    }
}
