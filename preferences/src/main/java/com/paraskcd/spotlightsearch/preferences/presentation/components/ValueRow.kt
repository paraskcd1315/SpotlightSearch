package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.BaseRowContainer
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.RowWithIcon
import com.paraskcd.spotlightsearch.designsystem.icons.ChevronRight
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun ValueRow(
    text: String,
    icon: ImageVector,
    shape: RoundedCornerShape,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit
) {
    BaseRowContainer(shape = shape, onClick = onClick) {
        RowWithIcon(text = text, icon = icon)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SettingsMetrics.TrailingSpacing)
        ) {
            trailing()
            Icon(ChevronRight, contentDescription = null)
        }
    }
}
