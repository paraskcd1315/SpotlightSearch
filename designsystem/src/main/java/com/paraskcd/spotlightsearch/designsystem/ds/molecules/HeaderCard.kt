package com.paraskcd.spotlightsearch.designsystem.ds.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import kotlin.math.max

@Composable
fun HeaderCard(title: String, icon: ImageVector? = null) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val shape = RoundedCornerShape(DsMetrics.CornerLarge)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = DsMetrics.HeaderOuterPadding,
                end = DsMetrics.HeaderOuterPadding,
                bottom = DsMetrics.HeaderOuterPadding
            ),
        color = Color.Transparent,
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .onSizeChanged { size = it }
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceTint.copy(alpha = DsMetrics.HeaderGlowInnerAlpha),
                            MaterialTheme.colorScheme.surfaceTint.copy(alpha = DsMetrics.HeaderGlowOuterAlpha),
                            MaterialTheme.colorScheme.surface
                        ),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = max(size.width, size.height).coerceAtLeast(1).toFloat() *
                            DsMetrics.HeaderGlowRadiusFactor
                    )
                )
                .padding(DsMetrics.HeaderInnerPadding)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = title,
                        modifier = Modifier
                            .padding(end = DsMetrics.IconSpacing)
                            .size(DsMetrics.HeaderIconSize),
                        tint = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
                Text(
                    title,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    }
}
