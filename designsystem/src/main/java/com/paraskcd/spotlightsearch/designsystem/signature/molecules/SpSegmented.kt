package com.paraskcd.spotlightsearch.designsystem.signature.molecules

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpSegment
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spGlassSurface
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing

@Composable
fun SpSegmented(
    labels: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    panel: Boolean = false
) {
    Box(
        modifier = modifier
            .spGlassSurface(SpShapes.pill, specular = panel, panel = panel)
            .padding(SpSpacing.s1)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(SpSpacing.s1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            labels.forEachIndexed { index, label ->
                SpSegment(label = label, active = index == selected, onClick = { onSelect(index) })
            }
        }
    }
}
