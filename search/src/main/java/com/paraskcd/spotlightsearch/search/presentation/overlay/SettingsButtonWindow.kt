package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.Gravity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spPanelSurface
import com.paraskcd.spotlightsearch.search.presentation.components.SettingsPill

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
        Box(
            modifier = Modifier
                .spPanelSurface(RoundedCornerShape(OverlayMetrics.SettingsButtonCornerRadius), blurred = blurEnabled)
                .clickableQuiet(onOpenSettings)
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
