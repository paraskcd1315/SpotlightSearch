package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.GroupSurface
import com.paraskcd.spotlightsearch.preferences.presentation.components.SectionTitle
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow

@Composable
fun ToggleScreen(
    title: String,
    label: String,
    icon: ImageVector,
    checked: Boolean?,
    onCheckedChange: (Boolean) -> Unit
) {
    LazyColumn {
        item { SectionTitle(title) }
        item {
            if (checked == null) {
                SettingsSkeleton()
                return@item
            }
            GroupSurface(count = 1) { _, shape ->
                SwitchRow(label, icon, checked, shape, onCheckedChange)
            }
        }
    }
}
