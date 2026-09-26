package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.atoms.SkeletonBlock
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpGroupedList
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpSpacing
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun SettingsSkeleton(rows: Int = SettingsMetrics.SkeletonRows) {
    SpGroupedList(count = rows) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SpMetrics.settingsListItemHeightTall)
                .padding(horizontal = SpSpacing.s4),
            horizontalArrangement = Arrangement.spacedBy(SpSpacing.s3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBlock(modifier = Modifier.size(SpMetrics.settingsIconWellSize), shape = CircleShape)
            Column(verticalArrangement = Arrangement.spacedBy(SpSpacing.s2), modifier = Modifier.fillMaxWidth()) {
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
