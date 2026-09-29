package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion

@Composable
fun rememberPanelCap(capPx: Int, keyboardMoving: Boolean): Int {
    val frozen = remember(keyboardMoving) { if (keyboardMoving) capPx else null }
    val target = frozen?.let { minOf(it, capPx) } ?: capPx
    val cap = remember { Animatable(target, Int.VectorConverter) }
    LaunchedEffect(target) {
        if (target <= cap.value) {
            cap.snapTo(target)
        } else {
            cap.animateTo(target, tween(SpMotion.durMorphMs, easing = SpMotion.easeIos))
        }
    }
    return minOf(cap.value, target)
}
