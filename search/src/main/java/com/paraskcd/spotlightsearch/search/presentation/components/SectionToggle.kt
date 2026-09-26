package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.search.R

@Composable
fun SectionToggle(expanded: Boolean, total: Int, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SpMetrics.touchTargetMin)
            .clickableQuiet(onToggle)
            .padding(horizontal = SpSpacing.s4),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = if (expanded) stringResource(R.string.section_show_less) else stringResource(R.string.section_show_all, total),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = SpTheme.colors.brandText
        )
    }
}
