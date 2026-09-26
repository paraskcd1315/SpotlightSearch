package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.Gravity
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.search.presentation.components.SettingsPill
import com.paraskcd.spotlightsearch.search.presentation.utils.surfaceAlpha

@Composable
fun SettingsButtonWindow(
    blurEnabled: Boolean,
    statusBarPx: Int,
    onOpenSettings: () -> Unit,
    onClose: () -> Unit,
    onBottomOnScreen: (Int) -> Unit
) {
    val density = LocalDensity.current
    val offsetX = with(density) { OverlayMetrics.SettingsButtonMargin.roundToPx() }
    val offsetY = statusBarPx + with(density) { OverlayMetrics.SettingsButtonTopMargin.roundToPx() }
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.SettingsButtonCornerRadius,
        onDismissRequest = onClose,
        gravity = Gravity.TOP or Gravity.END,
        offsetX = offsetX,
        wrapWidth = true
    ) {
        val view = LocalView.current
        val shape = RoundedCornerShape(OverlayMetrics.SettingsButtonCornerRadius)
        Surface(
            onClick = onOpenSettings,
            shape = shape,
            color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = surfaceAlpha(blurEnabled)),
            modifier = Modifier
                .border(DsMetrics.OutlineWidth, MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha), shape)
                .onGloballyPositioned { coordinates ->
                    val location = IntArray(2)
                    view.getLocationOnScreen(location)
                    onBottomOnScreen(location[1] + coordinates.size.height)
                }
        ) {
            SettingsPill()
        }
    }
}
