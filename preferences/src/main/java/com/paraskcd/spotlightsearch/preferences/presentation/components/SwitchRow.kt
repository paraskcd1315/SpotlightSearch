package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.BaseRowContainer
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.RowWithIcon

@Composable
fun SwitchRow(
    text: String,
    icon: ImageVector,
    checked: Boolean,
    shape: RoundedCornerShape,
    onCheckedChange: (Boolean) -> Unit
) {
    BaseRowContainer(shape = shape, onClick = { onCheckedChange(!checked) }) {
        RowWithIcon(text = text, icon = icon)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
