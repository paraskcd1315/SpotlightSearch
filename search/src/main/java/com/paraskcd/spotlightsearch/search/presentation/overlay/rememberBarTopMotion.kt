package com.paraskcd.spotlightsearch.search.presentation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun rememberBarTopMotion(): BarTopMotion {
    val motion = remember { BarTopMotion() }
    LaunchedEffect(motion) { motion.run() }
    return motion
}
