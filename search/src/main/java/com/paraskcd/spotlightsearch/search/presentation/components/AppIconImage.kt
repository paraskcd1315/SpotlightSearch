package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.composables.icons.lucide.Briefcase
import com.composables.icons.lucide.Lucide
import com.paraskcd.spotlightsearch.designsystem.signature.atoms.SpBadge
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader

@Composable
fun AppIconImage(
    packageName: String,
    loader: AppIconLoader,
    themed: Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
    profile: Long? = null
) {
    val tint = if (themed) SpTheme.colors.brandText.toArgb() else null
    val bitmap by produceState(loader.cached(packageName, profile, tint), packageName, profile, tint) {
        value = loader.load(packageName, profile, tint)
    }
    Box(modifier = modifier.size(size)) {
        Box(modifier = Modifier.size(size).clip(CircleShape)) {
            bitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(size))
            }
        }
        if (profile != null) {
            SpBadge(
                icon = Lucide.Briefcase,
                contentDescription = stringResource(R.string.work_app_badge),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
