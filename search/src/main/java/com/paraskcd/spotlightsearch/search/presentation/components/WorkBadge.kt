package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Briefcase
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpIconDisc
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics

@Composable
fun WorkBadge(contentDescription: String, modifier: Modifier = Modifier) {
    SpIconDisc(
        icon = Lucide.Briefcase,
        size = SpMetrics.badgeSize,
        contentDescription = contentDescription,
        modifier = modifier
    )
}
