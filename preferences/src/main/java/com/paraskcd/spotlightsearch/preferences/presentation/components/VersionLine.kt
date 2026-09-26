package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R

@Composable
fun VersionLine(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val info = remember(context) {
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0) }.getOrNull()
    }
    Text(
        text = stringResource(R.string.settings_version_line, info?.versionName.orEmpty(), info?.longVersionCode ?: 0L),
        style = MaterialTheme.typography.labelSmall,
        color = SpTheme.colors.textSecondary,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpSpacing.s4, vertical = SpSpacing.s2)
    )
}
