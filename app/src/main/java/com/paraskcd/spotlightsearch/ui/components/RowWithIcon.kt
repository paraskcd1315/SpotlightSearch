package com.paraskcd.spotlightsearch.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun RowWithIcon(icon: ImageVector? = null, iconDrawable: Drawable? = null, text: String, subtext: String? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon?.let {
            Surface(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(40.dp)
                    .width(40.dp),
                color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = it,
                    contentDescription = text,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                )
            }
        }
        iconDrawable?.let {
            Image(
                painter = rememberDrawablePainter(it),
                contentDescription = text,
                modifier = Modifier
                    .width(54.dp)
                    .height(54.dp)
                    .padding(6.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            )
        }
        Column {
            Text(text, style = MaterialTheme.typography.titleMedium)
            subtext?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}