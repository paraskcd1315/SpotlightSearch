package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.GripVertical
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R

@Composable
fun SectionToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    SwitchRow(
        text = label,
        checked = checked,
        onCheckedChange = onCheckedChange,
        leading = {
            Icon(
                Lucide.GripVertical,
                contentDescription = stringResource(R.string.quick_search_drag),
                tint = SpTheme.colors.textTertiary,
                modifier = Modifier.size(SpMetrics.settingsChevronSize)
            )
        }
    )
}
