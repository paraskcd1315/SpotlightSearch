package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.icons.PermDeviceInfo
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.presentation.model.RowMenuItem
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.hitText
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.actions.OpenAppInfo
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit

@Composable
fun AppRow(
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
        ResultRow(
            text = hitText(hit),
            onClick = onClick,
            onLongClick = { menuOpen = true },
            leading = {
                AppIconImage(hit.packageName, icons, themed = true, size = SearchMetrics.RowIconSize)
            }
        )
        RowContextMenu(expanded = menuOpen, items = menu, onDismiss = { menuOpen = false }, onAction = onAction)
    }
}
