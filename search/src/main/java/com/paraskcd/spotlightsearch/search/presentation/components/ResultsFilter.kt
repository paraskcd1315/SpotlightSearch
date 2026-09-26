package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSegmented
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.utils.titleRes

@Composable
fun ResultsFilter(
    kinds: List<SectionKind>,
    active: SectionKind?,
    onSelect: (SectionKind?) -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf(stringResource(R.string.filter_all)) + kinds.map { stringResource(it.titleRes()) }
    SpSegmented(
        labels = labels,
        selected = active?.let { kinds.indexOf(it) + 1 } ?: 0,
        onSelect = { index -> onSelect(kinds.getOrNull(index - 1)) },
        panel = true,
        modifier = modifier
    )
}
