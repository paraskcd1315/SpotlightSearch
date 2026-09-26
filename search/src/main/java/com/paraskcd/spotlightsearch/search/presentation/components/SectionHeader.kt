package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.titleRes

@Composable
fun SectionHeader(kind: SectionKind, padding: PaddingValues = PaddingValues(SearchMetrics.SectionHeaderPadding)) {
    Text(
        text = stringResource(kind.titleRes()),
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Black,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(padding)
    )
}
