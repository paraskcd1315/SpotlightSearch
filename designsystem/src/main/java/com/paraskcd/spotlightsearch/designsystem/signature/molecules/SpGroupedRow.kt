package com.paraskcd.spotlightsearch.designsystem.signature.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.GroupedCorners
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.LocalGroupedRowShape
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spGlassSurface

@Composable
fun SpGroupedRow(
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    inset: Dp = SpMetrics.groupedRowInset,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = GroupedCorners.of(index, count)
    CompositionLocalProvider(LocalGroupedRowShape provides shape) {
        Column(
            modifier = modifier
                .padding(horizontal = inset)
                .then(if (index > 0) Modifier.padding(top = SpMetrics.settingsListGap) else Modifier)
                .fillMaxWidth()
                .spGlassSurface(shape, specular = index == 0),
            content = content
        )
    }
}
