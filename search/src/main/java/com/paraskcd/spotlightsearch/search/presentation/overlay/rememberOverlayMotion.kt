package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekPhase
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekState

@Composable
fun rememberOverlayMotion(state: PeekState?, onCancelled: () -> Unit): OverlayMotion {
    val cancelled by rememberUpdatedState(onCancelled)
    val motion = remember { OverlayMotion(initiallyShown = state?.progress ?: 1f) }
    LaunchedEffect(state) {
        when (state?.phase) {
            null -> motion.rest()
            PeekPhase.Dragging -> motion.follow(state.progress, state.distancePx)
            PeekPhase.Committed -> motion.settle()
            PeekPhase.Cancelled -> {
                motion.leave()
                cancelled()
            }
        }
    }
    return motion
}
