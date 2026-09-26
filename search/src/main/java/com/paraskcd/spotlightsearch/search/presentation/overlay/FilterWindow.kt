package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.view.Gravity
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpRadii
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.components.ResultsFilter

@Composable
fun FilterWindow(
    kinds: List<SectionKind>,
    active: SectionKind?,
    onSelect: (SectionKind?) -> Unit,
    offsetX: Int,
    offsetY: Int,
    maxWidth: Dp,
    blurEnabled: Boolean,
    onClose: () -> Unit,
    onHeight: (Int) -> Unit
) {
    val visible = kinds.size > 1
    BlurredWindow(
        focusable = false,
        blurEnabled = blurEnabled,
        offsetY = offsetY,
        cornerRadius = SpRadii.pill,
        onDismissRequest = onClose,
        visible = visible,
        gravity = Gravity.BOTTOM or Gravity.START,
        offsetX = offsetX,
        wrapWidth = true,
        animateIn = true
    ) {
        if (!visible) return@BlurredWindow
        ResultsFilter(
            kinds = kinds,
            active = active,
            onSelect = onSelect,
            modifier = Modifier
                .widthIn(max = maxWidth)
                .onSizeChanged { onHeight(it.height) }
        )
    }
}
