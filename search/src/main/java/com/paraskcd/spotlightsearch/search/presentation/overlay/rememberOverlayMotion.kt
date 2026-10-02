package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekPhase

@Composable
fun rememberOverlayMotion(phase: PeekPhase?, onCancelled: () -> Unit): OverlayMotion {
    val cancelled by rememberUpdatedState(onCancelled)
    val motion = remember { OverlayMotion(initiallyShown = if (phase == null) 1f else 0f) }
    LaunchedEffect(phase) {
        when (phase) {
            null -> motion.rest()
            PeekPhase.Dragging -> Unit
            PeekPhase.Committed -> motion.settle()
            PeekPhase.Cancelled -> {
                motion.leave()
                cancelled()
            }
        }
    }
    return motion
}
