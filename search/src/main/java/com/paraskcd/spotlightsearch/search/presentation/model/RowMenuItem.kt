package com.paraskcd.spotlightsearch.search.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction

data class RowMenuItem(val label: String, val icon: ImageVector?, val action: HitAction)
