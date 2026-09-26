package com.paraskcd.spotlightsearch.preferences.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.composables.icons.lucide.ExternalLink
import com.composables.icons.lucide.Github
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ShieldCheck
import com.composables.icons.lucide.Star
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.layouts.SpScreenScaffold
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSectionHeader
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpSettingsRow
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.infrastructure.system.ExternalLinks
import com.paraskcd.spotlightsearch.preferences.presentation.components.AboutHero
import com.paraskcd.spotlightsearch.preferences.presentation.utils.AboutLinks

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val openFailed = stringResource(R.string.settings_open_failed)
    val open: (String) -> Unit = { url ->
        if (!ExternalLinks.open(context, url)) Toast.makeText(context, openFailed, Toast.LENGTH_SHORT).show()
    }
    SpScreenScaffold(
        title = stringResource(R.string.home_about_title),
        backDescription = stringResource(R.string.settings_back),
        onBack = onBack
    ) {
        item { AboutHero() }
        item { SpSectionHeader(stringResource(R.string.about_links_section)) }
        item {
            SpGroupedList(count = 3) { index ->
                when (index) {
                    0 -> SpSettingsRow(
                        label = stringResource(R.string.about_rate),
                        icon = Lucide.Star,
                        trailingIcon = Lucide.ExternalLink,
                        onClick = { open(AboutLinks.PLAY_STORE) }
                    )
                    1 -> SpSettingsRow(
                        label = stringResource(R.string.about_source),
                        caption = stringResource(R.string.about_source_caption),
                        icon = Lucide.Github,
                        trailingIcon = Lucide.ExternalLink,
                        onClick = { open(AboutLinks.SOURCE) }
                    )
                    else -> SpSettingsRow(
                        label = stringResource(R.string.about_privacy),
                        icon = Lucide.ShieldCheck,
                        trailingIcon = Lucide.ExternalLink,
                        onClick = { open(AboutLinks.PRIVACY) }
                    )
                }
            }
        }
        item {
            Text(
                text = stringResource(R.string.about_developer, stringResource(R.string.about_developer_name)),
                style = MaterialTheme.typography.labelMedium,
                color = SpTheme.colors.textTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SpMetrics.sectionGap)
            )
        }
    }
}
