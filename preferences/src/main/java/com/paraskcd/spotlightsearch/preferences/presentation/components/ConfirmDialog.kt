package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpButton
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpButtonVariant
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpBottomSheet
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R

@Composable
fun ConfirmDialog(
    visible: Boolean,
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    SpBottomSheet(
        visible = visible,
        onDismiss = onDismiss,
        title = title,
        footer = {
            SpButton(
                text = stringResource(R.string.dialog_cancel),
                onClick = onDismiss,
                variant = SpButtonVariant.Secondary,
                modifier = Modifier.weight(1f)
            )
            SpButton(
                text = confirmLabel,
                onClick = onConfirm,
                variant = SpButtonVariant.Danger,
                modifier = Modifier.weight(1f)
            )
        }
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge, color = SpTheme.colors.textSecondary)
    }
}
