package com.paraskcd.spotlightsearch.designsystem.signature.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpIconWell
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.LocalGroupedRowShape
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpSettingsRow(
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    caption: String? = null,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    trailingIcon: ImageVector? = if (onClick != null) Lucide.ChevronRight else null,
    maxLines: Int = 1
) {
    val colors = SpTheme.colors
    val shape = LocalGroupedRowShape.current ?: SpShapes.md
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (caption != null) SpMetrics.settingsListItemHeightTall else SpMetrics.settingsListItemHeight)
            .clip(shape)
            .then(if (onClick != null) Modifier.clickableQuiet(onClick) else Modifier)
            .padding(horizontal = SpSpacing.s4),
        horizontalArrangement = Arrangement.spacedBy(SpSpacing.s3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            leading != null -> leading()
            icon != null -> SpIconWell(icon)
        }
        Column(modifier = Modifier.weight(1f).padding(vertical = SpSpacing.s3)) {
            Text(
                text = label,
                fontSize = SpMetrics.settingsItemTextSize,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
            if (caption != null) {
                Text(
                    text = caption,
                    fontSize = SpMetrics.settingsCaptionTextSize,
                    color = colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        trailing?.invoke()
        if (trailingIcon != null) {
            Icon(trailingIcon, contentDescription = null, tint = colors.textTertiary, modifier = Modifier.size(SpMetrics.settingsChevronSize))
        }
    }
}
