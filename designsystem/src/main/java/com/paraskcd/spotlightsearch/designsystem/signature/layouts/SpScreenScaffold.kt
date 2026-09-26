package com.paraskcd.spotlightsearch.designsystem.signature.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.LocalSpHazeState
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpCollapsingHeader
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun SpScreenScaffold(
    title: String,
    backDescription: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    attachment: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val haze = rememberHazeState()
    var headerPx by remember { mutableIntStateOf(0) }
    var titlePx by remember { mutableIntStateOf(1) }
    val collapse by remember {
        derivedStateOf {
            when {
                listState.firstVisibleItemIndex > 0 -> 1f
                else -> (listState.firstVisibleItemScrollOffset / titlePx.toFloat()).coerceIn(0f, 1f)
            }
        }
    }
    val headerHeight = with(LocalDensity.current) { headerPx.toDp() }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SpMetrics.heroHeight)
                .graphicsLayer { alpha = 1f - collapse * HeroFade }
                .background(SpTheme.gradients.topGlow)
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(haze),
            contentPadding = PaddingValues(top = headerHeight, bottom = SpMetrics.bottomSpacer),
            content = content
        )
        CompositionLocalProvider(LocalSpHazeState provides haze) {
            SpCollapsingHeader(
                title = title,
                collapseFraction = collapse,
                backDescription = backDescription,
                onBack = onBack,
                onExpandedHeight = { total, titleHeight ->
                    if (collapse == 0f && total != headerPx) headerPx = total
                    if (titleHeight > 0) titlePx = titleHeight
                },
                modifier = Modifier.align(Alignment.TopCenter),
                attachment = attachment
            )
        }
    }
}

private const val HeroFade = 0.7f
