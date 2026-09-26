package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.designsystem.ds.atoms.IconCircle
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.hitIcon
import com.paraskcd.spotlightsearch.search.presentation.utils.hitText
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

@Composable
fun HitRow(
    hit: SearchHit,
    onClick: () -> Unit,
    leading: @Composable () -> Unit = { IconCircle(imageVector = hitIcon(hit), size = SearchMetrics.RowIconSize) }
) {
    ResultRow(text = hitText(hit), onClick = onClick, leading = leading)
}
