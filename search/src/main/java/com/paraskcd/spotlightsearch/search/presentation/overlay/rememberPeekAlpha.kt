package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekPhase
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekState

@Composable
fun rememberPeekAlpha(state: PeekState?, onCancelled: () -> Unit): Float {
    val cancelled by rememberUpdatedState(onCancelled)
    val shown = remember { Animatable(state?.progress ?: 1f) }
    LaunchedEffect(state) {
        when (state?.phase) {
            null -> shown.snapTo(1f)
            PeekPhase.Dragging -> shown.snapTo(state.progress)
            PeekPhase.Committed -> shown.animateTo(1f, tween(SpMotion.durMorphMs, easing = SpMotion.easeIos))
            PeekPhase.Cancelled -> {
                shown.animateTo(0f, tween(SpMotion.durMorphMs, easing = SpMotion.easeIos))
                cancelled()
            }
        }
    }
    return shown.value
}
