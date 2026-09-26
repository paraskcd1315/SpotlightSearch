package com.paraskcd.spotlightsearch.preferences.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.components.SettingsSkeleton
import com.paraskcd.spotlightsearch.preferences.presentation.components.SwitchRow

@Composable
fun ToggleScreen(
    title: String,
    label: String,
    icon: ImageVector,
    checked: Boolean?,
    onCheckedChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    SpScreenScaffold(title = title, backDescription = stringResource(R.string.settings_back), onBack = onBack) {
        item {
            if (checked == null) {
                SettingsSkeleton(rows = 1)
                return@item
            }
            SpGroupedList(count = 1) {
                SwitchRow(text = label, icon = icon, checked = checked, onCheckedChange = onCheckedChange)
            }
        }
    }
}
