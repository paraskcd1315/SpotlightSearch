package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.GroupedCorners
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.spGlassSurface

@Composable
fun GlassRow(index: Int, count: Int, blurEnabled: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .then(if (index > 0) Modifier.padding(top = SpMetrics.settingsListGap) else Modifier)
            .fillMaxWidth()
            .spGlassSurface(GroupedCorners.of(index, count), specular = index == 0, strong = !blurEnabled, panel = blurEnabled),
        content = content
    )
}
