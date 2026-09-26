package com.paraskcd.spotlightsearch.designsystem.signature.foundation

import androidx.compose.foundation.shape.RoundedCornerShape
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpRadii

object GroupedCorners {
    fun of(index: Int, count: Int): RoundedCornerShape {
        val first = index == 0
        val last = index == count - 1
        return RoundedCornerShape(
            topStart = if (first) SpRadii.lg else SpRadii.sm,
            topEnd = if (first) SpRadii.lg else SpRadii.sm,
            bottomStart = if (last) SpRadii.lg else SpRadii.sm,
            bottomEnd = if (last) SpRadii.lg else SpRadii.sm
        )
    }
}
