package com.paraskcd.spotlightsearch.preferences.presentation.utils

import androidx.compose.ui.unit.dp

object SettingsMetrics {
    val SectionTitlePadding = 16.dp
    val SheetListInset = 0.dp
    const val SectionTitleAlpha = 0.7f
    val RowSpacing = 16.dp
    val TrailingSpacing = 8.dp
    val SwatchWidth = 54.dp
    val SwatchHeight = 28.dp
    val SwatchSpacing = 12.dp
    val BottomSpacer = 12.dp
    val FilterFieldTopInset = 64.dp
    val FieldHorizontalPadding = 16.dp
    val FieldIconStart = 24.dp
    val FieldIconEnd = 8.dp
    val PillRadius = 100.dp
    val DialogOptionSpacing = 4.dp
    val DialogOptionRadius = 12.dp
    val PagePadding = 16.dp
    val PageSpacing = 20.dp
    val PreviewPadding = 16.dp
    val SliderSpacing = 4.dp
    val SliderTrackHeight = 16.dp
    val SliderVerticalPadding = 4.dp
    val ButtonSpacing = 4.dp
    const val SkeletonRows = 6
    const val SkeletonTitleFraction = 0.6f
    const val SkeletonSubtitleFraction = 0.4f
    val SkeletonTitleHeight = 16.dp
    val SkeletonSubtitleHeight = 12.dp
    const val BackgroundAlphaWithBlur = 0.5f
    const val BackgroundAlphaWithoutBlur = 0.9f
    const val WindowBlurRadius = 100

    fun backgroundAlpha(blurEnabled: Boolean): Float =
        if (blurEnabled) BackgroundAlphaWithBlur else BackgroundAlphaWithoutBlur
    const val SurfaceAlphaWithBlur = 0.65f
    const val EmptyTextAlpha = 0.5f
}
