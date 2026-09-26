package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem

@Composable
fun SettingsGroup(items: List<SettingPageItem>, onOpen: (String) -> Unit) {
    SpGroupedList(count = items.size) { index ->
        val item = items[index]
        SpSettingsRow(
            label = stringResource(item.title),
            caption = stringResource(item.subtitle),
            icon = item.icon,
            onClick = { onOpen(item.route) }
        )
    }
}
