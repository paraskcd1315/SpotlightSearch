package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.paraskcd.spotlightsearch.search.presentation.model.HitText
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultRow(
    text: HitText,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    leading: @Composable () -> Unit,
    below: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(SearchMetrics.RowPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.padding(end = SearchMetrics.RowIconSpacing)) { leading() }
            Column {
                Text(
                    text.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                text.subtitle?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SearchMetrics.SubtitleAlpha),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        below?.invoke()
    }
}
