package com.paraskcd.spotlightsearch.search.presentation.utils

fun surfaceAlpha(blurEnabled: Boolean): Float =
    if (blurEnabled) SearchMetrics.SurfaceAlphaWithBlur else SearchMetrics.SurfaceAlphaWithoutBlur
