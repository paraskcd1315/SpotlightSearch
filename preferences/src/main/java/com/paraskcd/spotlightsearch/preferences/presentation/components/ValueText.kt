package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun ValueText(value: String) {
    Text(value, style = MaterialTheme.typography.bodyMedium, color = SpTheme.colors.textSecondary)
}
