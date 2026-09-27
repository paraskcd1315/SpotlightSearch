package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.runtime.Composable
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpBottomSheet
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun <T> OptionSheet(
    visible: Boolean,
    title: String,
    options: List<T>,
    selected: T,
    label: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit
) {
    SpBottomSheet(visible = visible, onDismiss = onDismiss, title = title) {
        SpGroupedList(count = options.size, inset = SettingsMetrics.SheetListInset) { index ->
            val option = options[index]
            SpSettingsRow(
                label = label(option),
                onClick = { onSelect(option) },
                trailingIcon = if (option == selected) Lucide.Check else null
            )
        }
    }
}
