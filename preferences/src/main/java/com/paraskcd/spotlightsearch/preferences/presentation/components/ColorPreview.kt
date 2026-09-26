package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.RowWithIcon
import com.paraskcd.spotlightsearch.designsystem.icons.Palette
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun ColorPreview(
    hex: TextFieldValue,
    blurEnabled: Boolean,
    onHexChange: (TextFieldValue) -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val cardShape = RoundedCornerShape(DsMetrics.CornerLarge)
    val pillShape = RoundedCornerShape(SettingsMetrics.PillRadius)
    val surfaceAlpha = if (blurEnabled) SettingsMetrics.SurfaceAlphaWithBlur else 1f
    val backgroundAlpha = SettingsMetrics.backgroundAlpha(blurEnabled)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = scheme.background.copy(alpha = backgroundAlpha),
        contentColor = scheme.onSurface,
        shape = cardShape
    ) {
        Column(
            modifier = Modifier.padding(SettingsMetrics.PreviewPadding),
            verticalArrangement = Arrangement.spacedBy(SettingsMetrics.RowSpacing)
        ) {
            Surface(
                color = scheme.surfaceBright.copy(alpha = surfaceAlpha),
                contentColor = scheme.onSurface,
                shape = cardShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(DsMetrics.OutlineWidth, scheme.outline.copy(alpha = DsMetrics.OutlineAlpha), cardShape)
            ) {
                Column(modifier = Modifier.padding(DsMetrics.RowContentPadding)) {
                    RowWithIcon(text = stringResource(R.string.color_preview), icon = Palette, subtext = hex.text)
                }
            }
            TextField(
                value = hex,
                onValueChange = onHexChange,
                singleLine = true,
                shape = pillShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(DsMetrics.OutlineWidth, scheme.outline.copy(alpha = DsMetrics.OutlineAlpha), pillShape),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = scheme.surfaceBright.copy(alpha = surfaceAlpha),
                    unfocusedContainerColor = scheme.surfaceBright.copy(alpha = surfaceAlpha),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = scheme.onSurface,
                    focusedTextColor = scheme.onSurface,
                    unfocusedTextColor = scheme.onSurface
                )
            )
        }
    }
}
