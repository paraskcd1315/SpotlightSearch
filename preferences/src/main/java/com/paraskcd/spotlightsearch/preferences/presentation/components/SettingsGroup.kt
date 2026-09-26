package com.paraskcd.spotlightsearch.preferences.presentation.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem

@Composable
fun SettingsGroup(items: List<SettingPageItem>, onOpen: (SettingPageItem) -> Boolean) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.settings_coming_soon)
    SpGroupedList(count = items.size) { index ->
        val item = items[index]
        SpSettingsRow(
            label = stringResource(item.title),
            caption = stringResource(item.subtitle),
            icon = item.icon,
            onClick = {
                if (!onOpen(item)) Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
