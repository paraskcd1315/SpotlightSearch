package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.overlayDismissDrag(drag: OverlayDrag?): Modifier {
    if (drag == null) return this
    return nestedScroll(drag.connection).pointerInput(drag) {
        detectVerticalDragGestures(
            onVerticalDrag = { change, _ ->
                change.consume()
                drag.follow()
            },
            onDragEnd = drag::release,
            onDragCancel = drag::release
        )
    }
}
