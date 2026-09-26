package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.presentation.components.AppIconImage
import kotlin.math.roundToInt

@Composable
fun OverlayScrim(
    visible: Boolean,
    appName: String,
    icons: AppIconLoader,
    onClose: () -> Unit
) {
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val packageName = LocalContext.current.packageName
    val scrim = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = OverlayMetrics.ScrimAlpha),
            MaterialTheme.colorScheme.surface.copy(alpha = OverlayMetrics.ScrimAlpha)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = (1f - dragOffset / OverlayMetrics.DragFadeDistancePx).coerceIn(0f, 1f) }
            .background(scrim)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClose)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, amount ->
                        dragOffset = (dragOffset + amount).coerceAtLeast(0f)
                        change.consume()
                    },
                    onDragEnd = {
                        if (dragOffset > OverlayMetrics.DismissDragPx) onClose() else dragOffset = 0f
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(OverlayMetrics.EntryFadeMs)) + scaleIn(
                initialScale = OverlayMetrics.EntryInitialScale,
                animationSpec = tween(OverlayMetrics.EntryFadeMs, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.offset { IntOffset(0, dragOffset.roundToInt()) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(OverlayMetrics.LogoSpacing)
            ) {
                AppIconImage(packageName, icons, themed = false, size = OverlayMetrics.LogoSize)
                Text(
                    text = appName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
