package com.paraskcd.spotlightsearch.types

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.enums.SearchResultType

data class SearchResult(
    val title: String,
    val subtitle: String? = null,
    val icon: Drawable? = null,
    val iconVector: ImageVector? = null,
    val onClick: () -> Unit,
    val actionButtons: List<ActionButton>? = null,
    val isHeader: Boolean = false,
    val contextMenuActions: List<ContextMenuAction>? = null,
    val searchResultType: SearchResultType? = null,
    val hasTextChangeFlag: Boolean? = false
)
