package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.Gravity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spPanelSurface
import com.paraskcd.spotlightsearch.search.presentation.components.SettingsPill

@Composable
fun SettingsButtonWindow(
    visible: Boolean,
    blurEnabled: Boolean,
    offsetX: Int,
    offsetY: Int,
    onOpenSettings: () -> Unit,
    onClose: () -> Unit,
    onSize: (IntSize) -> Unit
) {
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.SettingsButtonCornerRadius,
        onDismissRequest = onClose,
        gravity = Gravity.BOTTOM or Gravity.END,
        offsetX = offsetX,
        wrapWidth = true,
        visible = visible,
        animateIn = true
    ) {
        Box(
            modifier = Modifier
                .spPanelSurface(RoundedCornerShape(OverlayMetrics.SettingsButtonCornerRadius), blurred = blurEnabled)
                .clickableQuiet(onOpenSettings)
                .onSizeChanged(onSize)
        ) {
            SettingsPill()
        }
    }
}
