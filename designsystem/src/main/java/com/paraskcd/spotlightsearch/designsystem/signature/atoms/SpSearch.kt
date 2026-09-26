package com.paraskcd.spotlightsearch.designsystem.signature.atoms

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpTouchTarget
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spGlassSurface
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpGlass
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpSearch(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    clearDescription: String,
    modifier: Modifier = Modifier
) {
    val colors = SpTheme.colors
    val focus = LocalFocusManager.current
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(SpMetrics.searchHeight)
            .spGlassSurface(SpShapes.pill, specular = false)
            .then(if (focused) Modifier.border(SpGlass.borderWidth, colors.brand, SpShapes.pill) else Modifier)
            .padding(start = SpSpacing.s4, end = SpSpacing.s1),
        horizontalArrangement = Arrangement.spacedBy(SpSpacing.s3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Lucide.Search, contentDescription = null, tint = colors.textTertiary, modifier = Modifier.size(SpMetrics.searchIconSize))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            interactionSource = interaction,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(colors.brand),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focus.clearFocus() }),
            decorationBox = { field ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(placeholder, style = MaterialTheme.typography.bodyLarge, color = colors.textTertiary)
                    }
                    field()
                }
            }
        )
        if (value.isNotEmpty()) {
            SpTouchTarget(onClick = { onValueChange("") }, minWidth = SpMetrics.searchHeight, minHeight = SpMetrics.searchHeight) {
                Icon(Lucide.X, contentDescription = clearDescription, tint = colors.textTertiary, modifier = Modifier.size(SpMetrics.clearIconSize))
            }
        }
    }
}
