package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.groupShape
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.surfaceAlpha

@Composable
fun SectionBlock(section: SearchSection, blurEnabled: Boolean, icons: IconSources, callbacks: HitCallbacks) {
    Column {
        SectionHeader(section.kind)
        section.hits.forEachIndexed { index, hit ->
            val shape = groupShape(index, section.hits.size)
            Surface(
                color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = surfaceAlpha(blurEnabled)),
                shape = shape,
                modifier = Modifier
                    .padding(vertical = SearchMetrics.RowGap)
                    .fillMaxWidth()
                    .border(
                        DsMetrics.OutlineWidth,
                        MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                        shape
                    )
            ) {
                HitContent(hit, icons, callbacks)
            }
        }
    }
}
