package com.paraskcd.spotlightsearch.designsystem.signature.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpTouchTarget
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpGlass
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val colors = SpTheme.colors
    val track by animateColorAsState(
        targetValue = if (checked) colors.brand else colors.surfaceContainerHigh,
        animationSpec = tween(SpMotion.durMorphMs, easing = SpMotion.easeIos),
        label = "track"
    )
    val inset = (SpMetrics.switchTrackHeight - SpMetrics.switchKnobSize) / 2
    val knobOffset by animateDpAsState(
        targetValue = if (checked) SpMetrics.switchTrackWidth - SpMetrics.switchKnobSize - inset else inset,
        animationSpec = tween(SpMotion.durMorphMs, easing = SpMotion.easeIos),
        label = "knob"
    )
    SpTouchTarget(onClick = { onCheckedChange(!checked) }, modifier = modifier, minWidth = SpMetrics.switchTrackWidth) {
        Box(
            modifier = Modifier
                .width(SpMetrics.switchTrackWidth)
                .height(SpMetrics.switchTrackHeight)
                .clip(SpShapes.pill)
                .background(track)
                .border(SpGlass.borderWidth, if (checked) colors.brand else colors.border, SpShapes.pill),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = knobOffset)
                    .size(SpMetrics.switchKnobSize)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}
