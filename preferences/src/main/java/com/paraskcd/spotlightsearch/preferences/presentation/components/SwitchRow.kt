package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpSwitch
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow

@Composable
fun SwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector? = null,
    caption: String? = null,
    leading: (@Composable () -> Unit)? = null
) {
    SpSettingsRow(
        label = text,
        icon = icon,
        caption = caption,
        leading = leading,
        onClick = { onCheckedChange(!checked) },
        trailing = { SpSwitch(checked = checked, onCheckedChange = onCheckedChange) },
        trailingIcon = null
    )
}
