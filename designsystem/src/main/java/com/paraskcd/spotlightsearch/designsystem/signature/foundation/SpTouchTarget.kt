package com.paraskcd.spotlightsearch.designsystem.signature.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpTouchTarget(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = SpMetrics.touchTargetMin,
    minHeight: Dp = SpMetrics.touchTargetMin,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .sizeIn(minWidth = minWidth, minHeight = minHeight)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}
