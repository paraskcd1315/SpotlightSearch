package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Tag
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R

@Composable
fun VersionRow() {
    val context = LocalContext.current
    val version = remember(context) {
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0).versionName }.getOrNull().orEmpty()
    }
    SpGroupedList(count = 1) {
        SpSettingsRow(
            label = stringResource(R.string.settings_version),
            icon = Lucide.Tag,
            trailing = {
                Text(version, style = MaterialTheme.typography.bodyMedium, color = SpTheme.colors.textSecondary)
            }
        )
    }
}
