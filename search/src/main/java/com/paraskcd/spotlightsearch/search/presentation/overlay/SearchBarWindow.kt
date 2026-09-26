package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spPanelSurface
import com.paraskcd.spotlightsearch.search.presentation.components.SearchBarPill
import kotlinx.coroutines.android.awaitFrame

@Composable
fun SearchBarWindow(
    visible: Boolean,
    query: String,
    blurEnabled: Boolean,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onClose: () -> Unit,
    onTopOnScreen: (Int) -> Unit,
    onKeyboardShown: () -> Unit,
    onKeyboardVisibility: (Boolean) -> Unit
) {
    val offsetY = with(LocalDensity.current) { OverlayMetrics.BarBottomMargin.roundToPx() }
    BlurredWindow(
        focusable = true,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = OverlayMetrics.BarCornerRadius,
        onDismissRequest = onClose,
        visible = visible,
        animateIn = true
    ) {
        val view = LocalView.current
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            awaitFrame()
            focusRequester.requestFocus()
        }
        val tracker = remember(view) { KeyboardTracker(view, onTopOnScreen, onKeyboardShown, onKeyboardVisibility) }
        DisposableEffect(tracker) {
            val root = view.rootView
            root.setWindowInsetsAnimationCallback(tracker)
            onDispose { root.setWindowInsetsAnimationCallback(null) }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .spPanelSurface(RoundedCornerShape(OverlayMetrics.BarCornerRadius), blurred = blurEnabled)
                .onGloballyPositioned { tracker.onRest() }
        ) {
            SearchBarPill(
                query = query,
                focusRequester = focusRequester,
                onQueryChange = onQueryChange,
                onSubmit = onSubmit
            )
        }
    }
}
