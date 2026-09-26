package com.paraskcd.spotlightsearch.preferences.presentation.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.GroupSurface
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.model.SettingPageItem

@Composable
fun SettingsGroup(
    items: List<SettingPageItem>,
    trailingIcon: ImageVector,
    onOpen: (SettingPageItem) -> Boolean
) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.settings_coming_soon)
    GroupSurface(count = items.size) { index, shape ->
        val item = items[index]
        NavigationRow(
            title = stringResource(item.title),
            subtitle = stringResource(item.subtitle),
            icon = item.icon,
            trailingIcon = trailingIcon,
            shape = shape,
            onClick = {
                if (!onOpen(item)) Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
