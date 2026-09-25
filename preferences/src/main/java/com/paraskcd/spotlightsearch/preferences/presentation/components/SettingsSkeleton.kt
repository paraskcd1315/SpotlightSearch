package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.atoms.SkeletonBlock
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.BaseRowContainer
import com.paraskcd.spotlightsearch.designsystem.ds.molecules.GroupSurface
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun SettingsSkeleton() {
    GroupSurface(count = SettingsMetrics.SkeletonRows) { _, shape ->
        BaseRowContainer(shape = shape) {
            SkeletonBlock(modifier = Modifier.size(DsMetrics.IconTileSize), shape = CircleShape)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(DsMetrics.IconSpacing)
            ) {
                SkeletonBlock(
                    modifier = Modifier
                        .fillMaxWidth(SettingsMetrics.SkeletonTitleFraction)
                        .height(SettingsMetrics.SkeletonTitleHeight)
                )
                SkeletonBlock(
                    modifier = Modifier
                        .fillMaxWidth(SettingsMetrics.SkeletonSubtitleFraction)
                        .height(SettingsMetrics.SkeletonSubtitleHeight)
                )
            }
        }
    }
}
