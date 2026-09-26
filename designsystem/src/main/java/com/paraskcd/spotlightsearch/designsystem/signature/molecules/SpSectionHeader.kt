package com.paraskcd.spotlightsearch.designsystem.signature.molecules

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpSectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = SpTheme.colors.textSecondary,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = SpSpacing.s5, end = SpSpacing.s5, top = SpSpacing.s5, bottom = SpSpacing.s2)
    )
}
