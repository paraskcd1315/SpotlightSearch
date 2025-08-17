package com.paraskcd.spotlightsearch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun HeaderCard(title: String, subtitle: String? = null, icon: ImageVector? = null) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .onSizeChanged { size = it }
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.55f),
                            MaterialTheme.colorScheme.surface
                        ),
                        center = Offset(
                            (size.width / 2f),
                            (size.height / 2f)
                        ),
                        radius = (max(size.width, size.height)
                            .coerceAtLeast(1))   // evita 0
                            .toFloat() * 0.95f
                    )
                )
                .padding(72.dp) // mismo padding interno
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(40.dp)
                            .width(40.dp)
                            .align(Alignment.CenterVertically),
                        tint = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
                Text("Spotlight Search", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.inverseOnSurface)
            }
        }
    }
}