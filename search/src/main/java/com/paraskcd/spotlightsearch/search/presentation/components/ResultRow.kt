package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.search.presentation.model.HitText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultRow(
    text: HitText,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    leading: @Composable () -> Unit,
    below: (@Composable () -> Unit)? = null
) {
    val colors = SpTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = SpSpacing.s4, vertical = SpSpacing.s3)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SpSpacing.s3)) {
            leading()
            Column {
                Text(
                    text.title,
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = SpMetrics.settingsItemTextSize,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                text.subtitle?.let {
                    Text(
                        it,
                        color = colors.textSecondary,
                        fontSize = SpMetrics.settingsCaptionTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        below?.invoke()
    }
}
