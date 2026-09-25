package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun FilterField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(SettingsMetrics.PillRadius)
    val onSurface = MaterialTheme.colorScheme.onSurface
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SettingsMetrics.FieldHorizontalPadding)
            .border(
                width = DsMetrics.OutlineWidth,
                color = MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                shape = shape
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = onSurface,
            focusedTextColor = onSurface,
            unfocusedTextColor = onSurface
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = onSurface,
                modifier = Modifier.padding(start = SettingsMetrics.FieldIconStart, end = SettingsMetrics.FieldIconEnd)
            )
        }
    )
}
