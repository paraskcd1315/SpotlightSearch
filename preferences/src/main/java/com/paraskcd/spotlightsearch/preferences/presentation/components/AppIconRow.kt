package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader
import com.paraskcd.spotlightsearch.search.presentation.components.AppIconImage
import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp

@Composable
fun AppIconRow(app: InstalledApp, custom: Boolean, icons: AppIconLoader, onClick: () -> Unit) {
    SpSettingsRow(
        label = app.label,
        caption = app.packageName,
        onClick = onClick,
        leading = {
            AppIconImage(
                packageName = app.packageName,
                loader = icons,
                themed = true,
                size = SpMetrics.settingsIconWellSize,
                profile = app.profile
            )
        },
        trailing = if (custom) ({ ValueText(stringResource(R.string.app_icons_custom)) }) else null
    )
}
