package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.BaseRowContainer
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.presentation.components.AppIconImage

@Composable
fun AppToggleRow(
    packageName: String,
    label: String,
    checked: Boolean,
    icons: AppIconLoader,
    shape: RoundedCornerShape,
    onCheckedChange: (Boolean) -> Unit,
    isDragging: Boolean = false,
    dragIcon: ImageVector? = null
) {
    BaseRowContainer(shape = shape, isDragging = isDragging) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SettingsMetrics.RowSpacing)
        ) {
            dragIcon?.let { Icon(imageVector = it, contentDescription = stringResource(R.string.quick_search_drag)) }
            AppIconImage(packageName = packageName, loader = icons, themed = false, size = DsMetrics.IconTileSize)
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(packageName, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
