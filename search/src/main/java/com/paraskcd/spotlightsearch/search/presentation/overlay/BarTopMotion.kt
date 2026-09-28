package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@Stable
class BarTopMotion {
    var top by mutableStateOf<Int?>(null)
        private set
    private var settleTarget by mutableStateOf<Int?>(null)

    fun follow(value: Int) {
        settleTarget = null
        top = value
    }

    fun settle(value: Int) {
        if (top == null) top = value else settleTarget = value
    }

    suspend fun run() {
        snapshotFlow { settleTarget }.collectLatest { target ->
            val from = top
            if (target == null || from == null || from == target) return@collectLatest
            animate(
                initialValue = from.toFloat(),
                targetValue = target.toFloat(),
                animationSpec = tween(SpMotion.durMorphMs, easing = SpMotion.easeIos)
            ) { value, _ -> top = value.roundToInt() }
        }
    }
}
