package com.paraskcd.spotlightsearch

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun BlurredBackground(
    screenshot: Bitmap?,
    modifier: Modifier = Modifier,
    blurRadius: Float = 30f
) {
    screenshot?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.blur(blurRadius.dp)
        )
    }
}