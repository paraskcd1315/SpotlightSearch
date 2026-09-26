package com.paraskcd.spotlightsearch.preferences.presentation.components

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun AboutHero() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val appName = remember(context) { context.applicationInfo.loadLabel(packageManager).toString() }
    val icon = remember(context) {
        val drawable = packageManager.getApplicationIcon(context.packageName)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        drawable.setBounds(0, 0, bitmap.width, bitmap.height)
        drawable.draw(Canvas(bitmap))
        bitmap.asImageBitmap()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SettingsMetrics.AboutHeroPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SettingsMetrics.AboutHeroSpacing)
    ) {
        Image(
            bitmap = icon,
            contentDescription = null,
            modifier = Modifier
                .size(SettingsMetrics.AboutIconSize)
                .clip(CircleShape)
        )
        Text(
            text = appName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SpTheme.colors.textPrimary
        )
        VersionLine(textAlign = TextAlign.Center)
        Text(
            text = stringResource(R.string.about_description),
            style = MaterialTheme.typography.bodyMedium,
            color = SpTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}
