package com.paraskcd.spotlightsearch.designsystem.signature.organisms

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spGlassSurface
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpRadii
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = SpTheme.colors
    val shape = RoundedCornerShape(topStart = SpRadii.xl, topEnd = SpRadii.xl)
    val scrimState = remember { MutableTransitionState(false) }.apply { targetState = visible }
    val panelState = remember { MutableTransitionState(false) }.apply { targetState = visible }
    if (visible) BackHandler(onBack = onDismiss)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visibleState = scrimState,
            enter = fadeIn(tween(SpMotion.durMorphMs, easing = SpMotion.easeIos)),
            exit = fadeOut(tween(SpMotion.durMorphMs, easing = SpMotion.easeIos))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.scrim)
                    .clickableQuiet(onDismiss)
            )
        }
        AnimatedVisibility(
            visibleState = panelState,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(SpMotion.durPushMs, easing = SpMotion.easeIos)) { it } +
                fadeIn(tween(SpMotion.durMorphMs)),
            exit = slideOutVertically(tween(SpMotion.durPushMs, easing = SpMotion.easeIos)) { it } +
                fadeOut(tween(SpMotion.durMorphMs))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .spGlassSurface(shape, strong = true)
                    .clickableQuiet {}
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(start = SpSpacing.s5, end = SpSpacing.s5, bottom = SpSpacing.s5),
                verticalArrangement = Arrangement.spacedBy(SpSpacing.s4)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = SpSpacing.s2)
                        .size(SpMetrics.sheetHandleWidth, SpMetrics.sheetHandleHeight)
                        .clip(SpShapes.pill)
                        .background(colors.border)
                )
                Text(text = title, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
                content()
                if (footer != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpSpacing.s3),
                        content = footer
                    )
                }
            }
        }
    }
}
