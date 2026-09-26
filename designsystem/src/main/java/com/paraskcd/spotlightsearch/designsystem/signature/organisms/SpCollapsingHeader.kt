package com.paraskcd.spotlightsearch.designsystem.signature.organisms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.LocalSpHazeState
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpTouchTarget
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spHazeBlur
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpCollapsingHeader(
    title: String,
    collapseFraction: Float,
    backDescription: String,
    onBack: () -> Unit,
    onExpandedHeight: (total: Int, title: Int) -> Unit,
    modifier: Modifier = Modifier,
    attachment: (@Composable () -> Unit)? = null
) {
    val colors = SpTheme.colors
    val haze = LocalSpHazeState.current
    val f = collapseFraction.coerceIn(0f, 1f)
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val mirror = LocalLayoutDirection.current == LayoutDirection.Rtl
    var barPx by remember { mutableIntStateOf(0) }
    var titlePx by remember { mutableIntStateOf(0) }
    var attachmentPx by remember { mutableIntStateOf(0) }
    val report = { onExpandedHeight(barPx + titlePx + attachmentPx, titlePx) }

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = f }
                .then(if (haze != null) Modifier.spHazeBlur(haze, colors) else Modifier.background(colors.glassStrongBg))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(SpMetrics.hairlineThickness)
                .graphicsLayer { alpha = f }
                .background(colors.hairline)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { barPx = it.height; report() }
                    .padding(top = topInset)
                    .height(SpMetrics.collapsingBarHeight)
                    .padding(horizontal = SpSpacing.s2),
                contentAlignment = Alignment.Center
            ) {
                SpTouchTarget(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        imageVector = Lucide.ChevronLeft,
                        contentDescription = backDescription,
                        tint = colors.textPrimary,
                        modifier = Modifier
                            .size(SpMetrics.headerIconSize)
                            .graphicsLayer { scaleX = if (mirror) -1f else 1f }
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = SpMetrics.touchTargetMin + SpSpacing.s2)
                        .graphicsLayer { alpha = f }
                )
            }
            SpLargeTitle(title = title, fraction = f, onMeasured = { titlePx = it; report() })
            AnimatedContent(
                targetState = attachment,
                modifier = Modifier.onSizeChanged { attachmentPx = it.height; report() },
                transitionSpec = {
                    fadeIn(tween(SpMotion.durMorphMs, easing = SpMotion.easeIos)) togetherWith
                        fadeOut(tween(SpMotion.durMorphMs / 2, easing = SpMotion.easeIos)) using
                        SizeTransform(clip = false) { _, _ -> tween(SpMotion.durMorphMs, easing = SpMotion.easeIos) }
                },
                label = "attachment"
            ) { slot ->
                if (slot != null) {
                    Box(modifier = Modifier.padding(start = SpSpacing.s4, end = SpSpacing.s4, bottom = SpSpacing.s3)) { slot() }
                }
            }
        }
    }
}
