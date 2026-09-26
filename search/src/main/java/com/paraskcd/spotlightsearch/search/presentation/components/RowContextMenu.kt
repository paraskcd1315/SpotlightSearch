package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.search.presentation.model.RowMenuItem
import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction

@Composable
fun RowContextMenu(
    expanded: Boolean,
    items: List<RowMenuItem>,
    onDismiss: () -> Unit,
    onAction: (HitAction) -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.label, color = MaterialTheme.colorScheme.onSurface) },
                onClick = {
                    onDismiss()
                    onAction(item.action)
                },
                leadingIcon = item.icon?.let { icon ->
                    { Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) }
                }
            )
        }
    }
}
