package com.paraskcd.spotlightsearch.designsystem.ds.foundation

import androidx.compose.foundation.shape.RoundedCornerShape

fun groupShape(index: Int, count: Int): RoundedCornerShape = when {
    count == 1 -> RoundedCornerShape(DsMetrics.CornerLarge)
    index == 0 -> RoundedCornerShape(
        topStart = DsMetrics.CornerLarge,
        topEnd = DsMetrics.CornerLarge,
        bottomStart = DsMetrics.CornerSmall,
        bottomEnd = DsMetrics.CornerSmall
    )
    index == count - 1 -> RoundedCornerShape(
        topStart = DsMetrics.CornerSmall,
        topEnd = DsMetrics.CornerSmall,
        bottomStart = DsMetrics.CornerLarge,
        bottomEnd = DsMetrics.CornerLarge
    )
    else -> RoundedCornerShape(DsMetrics.CornerSmall)
}
