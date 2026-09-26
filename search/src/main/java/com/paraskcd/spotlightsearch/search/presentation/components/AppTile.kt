package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.paraskcd.spotlightsearch.designsystem.icons.PermDeviceInfo
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.presentation.model.RowMenuItem
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenAppInfo
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTile(
    hit: AppHit,
    icons: AppIconLoader,
    onClick: () -> Unit,
    onAction: (HitAction) -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }
    val menu = listOf(
        RowMenuItem(stringResource(R.string.action_app_info), PermDeviceInfo, OpenAppInfo(hit.packageName))
    )
    Box {
        Column(
            modifier = Modifier
                .width(SearchMetrics.TileWidth)
                .clip(RoundedCornerShape(SearchMetrics.TileCornerRadius))
                .combinedClickable(onClick = onClick, onLongClick = { menuOpen = true })
                .padding(SearchMetrics.TilePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SearchMetrics.TileSpacing)
        ) {
            AppIconImage(
                packageName = hit.packageName,
                loader = icons,
                themed = true,
                size = SearchMetrics.TileIconSize,
                modifier = Modifier.padding(SearchMetrics.TileIconInset)
            )
            Text(
                text = hit.label,
                style = MaterialTheme.typography.labelSmall,
                maxLines = SearchMetrics.TileLabelMaxLines,
                lineHeight = SearchMetrics.TileLabelLineHeight.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        RowContextMenu(expanded = menuOpen, items = menu, onDismiss = { menuOpen = false }, onAction = onAction)
    }
}
