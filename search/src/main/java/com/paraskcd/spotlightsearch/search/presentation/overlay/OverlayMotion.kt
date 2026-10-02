package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion

@Stable
class OverlayMotion(initiallyShown: Float = 1f) {
    private val shown = Animatable(initiallyShown)
    private var distancePx by mutableFloatStateOf(0f)

    var anchored by mutableStateOf(false)

    var leaving by mutableStateOf(false)
        private set

    val shiftPx: Float
        get() = if (anchored) (1f - shown.value) * distancePx else 0f

    val alpha: Float
        get() = when {
            distancePx <= 0f -> shown.value
            !anchored -> 0f
            else -> (shown.value * OverlayMetrics.PeekAlphaLead).coerceAtMost(1f)
        }

    val scrimAlpha: Float
        get() = if (distancePx > 0f && !anchored) 0f else shown.value

    val moving: Boolean by derivedStateOf { shiftPx != 0f }

    suspend fun rest() {
        shown.snapTo(1f)
    }

    suspend fun follow(progress: Float, distancePx: Float) {
        this.distancePx = distancePx
        shown.snapTo(progress.coerceIn(0f, 1f))
    }

    suspend fun settle(onFrame: (Float) -> Unit = {}) {
        shown.animateTo(1f, tween(SpMotion.durMorphMs, easing = SpMotion.easeIos)) { onFrame(value) }
    }

    suspend fun leave(onFrame: (Float) -> Unit = {}) {
        leaving = true
        shown.animateTo(0f, tween(SpMotion.durMorphMs, easing = SpMotion.easeIos)) { onFrame(value) }
    }
}
