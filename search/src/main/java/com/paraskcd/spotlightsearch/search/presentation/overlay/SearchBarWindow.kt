package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.search.presentation.components.SearchBarPill
import com.paraskcd.spotlightsearch.search.presentation.utils.surfaceAlpha
import kotlinx.coroutines.android.awaitFrame

@Composable
fun SearchBarWindow(
    query: String,
    blurEnabled: Boolean,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onOpenSettings: () -> Unit,
    onClose: () -> Unit,
    onTopOnScreen: (Int) -> Unit
) {
    val offsetY = with(LocalDensity.current) { OverlayMetrics.BarBottomMargin.roundToPx() }
    BlurredWindow(
        focusable = true,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.BarCornerRadius,
        onDismissRequest = onClose
    ) {
        val view = LocalView.current
        val focusRequester = remember { FocusRequester() }
        val shape = RoundedCornerShape(OverlayMetrics.BarCornerRadius)
        LaunchedEffect(Unit) {
            awaitFrame()
            focusRequester.requestFocus()
        }
        Surface(
            shape = shape,
            color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = surfaceAlpha(blurEnabled)),
            modifier = Modifier
                .fillMaxWidth()
                .border(DsMetrics.OutlineWidth, MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha), shape)
                .onGloballyPositioned {
                    val location = IntArray(2)
                    view.getLocationOnScreen(location)
                    onTopOnScreen(location[1])
                }
        ) {
            SearchBarPill(
                query = query,
                focusRequester = focusRequester,
                onQueryChange = onQueryChange,
                onSubmit = onSubmit,
                onOpenSettings = onOpenSettings,
                onClose = onClose
            )
        }
    }
}
