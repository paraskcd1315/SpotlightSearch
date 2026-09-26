package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpBottomSheet
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import com.paraskcd.spotlightsearch.preferences.presentation.utils.labelRes
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun ThemeModeDialog(visible: Boolean, selected: ThemeMode, onSelect: (ThemeMode) -> Unit, onDismiss: () -> Unit) {
    val modes = ThemeMode.entries
    SpBottomSheet(visible = visible, onDismiss = onDismiss, title = stringResource(R.string.theme_dialog_title)) {
        SpGroupedList(count = modes.size, inset = SettingsMetrics.SheetListInset) { index ->
            val mode = modes[index]
            SpSettingsRow(
                label = stringResource(mode.labelRes()),
                onClick = { onSelect(mode) },
                trailingIcon = if (mode == selected) Lucide.Check else null
            )
        }
    }
}
