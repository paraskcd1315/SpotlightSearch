package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow

@Composable
fun ValueRow(text: String, icon: ImageVector, onClick: () -> Unit, trailing: @Composable () -> Unit) {
    SpSettingsRow(label = text, icon = icon, onClick = onClick, trailing = trailing)
}
