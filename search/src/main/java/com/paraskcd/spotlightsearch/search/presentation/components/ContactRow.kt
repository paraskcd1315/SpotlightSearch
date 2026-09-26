package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.search.infrastructure.icons.ContactPhotoLoader
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.hitText
import com.paraskcd.spotlightsearch.sources.domain.model.actions.CopyNumber
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit

@Composable
fun ContactRow(
    hit: ContactHit,
    photos: ContactPhotoLoader,
    onClick: () -> Unit,
    onAction: (HitAction) -> Unit
) {
    ResultRow(
        text = hitText(hit),
        onClick = onClick,
        onLongClick = { onAction(CopyNumber(hit.number)) },
        leading = { ContactPhoto(hit.photoUri, photos, SearchMetrics.RowIconSize) },
        below = { ContactActions(hit, onAction) }
    )
}
