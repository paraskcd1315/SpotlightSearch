package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.paraskcd.spotlightsearch.designsystem.R

@OptIn(ExperimentalTextApi::class)
private fun quicksand(weight: Int) = Font(
    resId = R.font.quicksand,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight))
)

val Quicksand = FontFamily(quicksand(400), quicksand(500), quicksand(600), quicksand(700))

private val Base = Typography()

val SpTypography = Typography(
    displayLarge = Base.displayLarge.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold),
    displayMedium = Base.displayMedium.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold),
    displaySmall = Base.displaySmall.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold),
    headlineLarge = Base.headlineLarge.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 40.sp),
    headlineMedium = Base.headlineMedium.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold),
    headlineSmall = Base.headlineSmall.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold),
    titleLarge = Base.titleLarge.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = Base.titleMedium.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold, fontSize = 19.sp, lineHeight = 25.sp),
    titleSmall = Base.titleSmall.copy(fontFamily = Quicksand, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 18.sp),
    bodyLarge = Base.bodyLarge.copy(fontFamily = Quicksand, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium = Base.bodyMedium.copy(fontFamily = Quicksand, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 18.sp),
    bodySmall = Base.bodySmall.copy(fontFamily = Quicksand, fontWeight = FontWeight.Medium),
    labelLarge = Base.labelLarge.copy(fontFamily = Quicksand, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp),
    labelMedium = Base.labelMedium.copy(fontFamily = Quicksand, fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
    labelSmall = Base.labelSmall.copy(fontFamily = Quicksand, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp)
)

val SpEyebrow = TextStyle(
    fontFamily = Quicksand,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 1.4.sp
)
