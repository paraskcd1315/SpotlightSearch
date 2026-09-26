package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.presentation.components.AppIconImage

@Composable
fun AppToggleRow(
    packageName: String,
    label: String,
    checked: Boolean,
    icons: AppIconLoader,
    onCheckedChange: (Boolean) -> Unit,
    dragIcon: ImageVector? = null
) {
    SwitchRow(
        text = label,
        caption = packageName,
        checked = checked,
        onCheckedChange = onCheckedChange,
        leading = {
            Row(horizontalArrangement = Arrangement.spacedBy(SpSpacing.s2), verticalAlignment = Alignment.CenterVertically) {
                dragIcon?.let {
                    Icon(
                        it,
                        contentDescription = stringResource(R.string.quick_search_drag),
                        tint = SpTheme.colors.textTertiary,
                        modifier = Modifier.size(SpMetrics.settingsChevronSize)
                    )
                }
                AppIconImage(packageName = packageName, loader = icons, themed = false, size = SpMetrics.settingsIconWellSize)
            }
        }
    )
}
