package com.paraskcd.spotlightsearch.designsystem.signature.atoms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpGlass
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: SpButtonVariant = SpButtonVariant.Primary,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val colors = SpTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) SpMetrics.buttonPressScale else 1f,
        animationSpec = tween(SpMotion.durMorphMs, easing = SpMotion.easeIos),
        label = "press"
    )
    val fill = when (variant) {
        SpButtonVariant.Primary -> Modifier.background(SpTheme.gradients.buttonFill)
        SpButtonVariant.Secondary -> Modifier
            .background(colors.glassBg)
            .border(SpGlass.borderWidth, colors.glassBorder, SpShapes.pill)
        SpButtonVariant.Danger -> Modifier
            .background(colors.danger.copy(alpha = SpMetrics.dangerFillAlpha))
            .border(SpGlass.borderWidth, colors.danger.copy(alpha = SpMetrics.dangerBorderAlpha), SpShapes.pill)
        SpButtonVariant.Ghost -> Modifier
    }
    val content = when (variant) {
        SpButtonVariant.Primary -> Color.White
        SpButtonVariant.Secondary -> colors.textPrimary
        SpButtonVariant.Danger -> colors.dangerText
        SpButtonVariant.Ghost -> colors.brandText
    }
    Box(
        modifier = modifier
            .height(SpMetrics.buttonHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (enabled) 1f else SpMetrics.disabledAlpha
            }
            .clip(SpShapes.pill)
            .then(fill)
            .clickable(enabled = enabled, interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = SpMetrics.buttonHorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpSpacing.s2, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { Icon(it, contentDescription = null, tint = content, modifier = Modifier.size(SpMetrics.buttonIconSize)) }
            Text(text = text, color = content, style = MaterialTheme.typography.labelLarge, maxLines = 1)
        }
    }
}
