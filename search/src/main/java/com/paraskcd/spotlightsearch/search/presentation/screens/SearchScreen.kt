package com.paraskcd.spotlightsearch.search.presentation.screens

import android.view.HapticFeedbackConstants
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import com.paraskcd.spotlightsearch.search.infrastructure.window.DialogWindowSetup
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.HitOutcome
import com.paraskcd.spotlightsearch.search.presentation.overlay.OverlayMetrics
import com.paraskcd.spotlightsearch.search.presentation.overlay.OverlayScrim
import com.paraskcd.spotlightsearch.search.presentation.overlay.ResultsWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.SearchBarWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.SettingsButtonWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.FrequentAppsWindow
import com.paraskcd.spotlightsearch.search.presentation.model.SearchResults
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.search.presentation.viewmodels.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    blurEnabled: Boolean,
    appName: String,
    onOpenSettings: () -> Unit,
    onClose: () -> Unit
) {
    val results by viewModel.results.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var barTop by remember { mutableStateOf<Int?>(null) }
    var settingsBottom by remember { mutableStateOf<Int?>(null) }
    var frequentHeight by remember { mutableStateOf<Int?>(null) }
    val view = LocalView.current
    val density = LocalDensity.current
    val displayHeightPx = LocalActivity.current?.window?.let(DialogWindowSetup::displayHeight) ?: 0
    val statusBarPx = WindowInsets.statusBars.getTop(density)
    val gapPx = with(density) { OverlayMetrics.ResultsGap.roundToPx() }

    val onQueryChange: (String) -> Unit = { query ->
        text = query
        viewModel.onQueryChange(query)
    }
    val handle: (HitOutcome) -> Unit = { outcome ->
        when (outcome) {
            HitOutcome.Close -> onClose()
            HitOutcome.Stay -> Unit
            is HitOutcome.ReplaceQuery -> text = outcome.query
        }
    }
    val callbacks = HitCallbacks(
        onHitClick = { hit -> handle(viewModel.onHitClick(hit)) },
        onAction = { action -> handle(viewModel.run(action)) }
    )

    LaunchedEffect(Unit) {
        delay(OverlayMetrics.EntryDelayMs)
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        visible = true
    }

    OverlayScrim(
        visible = visible,
        appName = appName,
        icons = viewModel.icons.apps,
        topLimitPx = settingsBottom,
        bottomLimitPx = barTop?.let { bar -> frequentHeight?.let { bar - gapPx - it } ?: bar },
        onClose = onClose
    )

    if (!visible) return

    SettingsButtonWindow(
        blurEnabled = blurEnabled,
        statusBarPx = statusBarPx,
        onOpenSettings = onOpenSettings,
        onClose = onClose,
        onBottomOnScreen = { settingsBottom = it }
    )

    SearchBarWindow(
        query = text,
        blurEnabled = blurEnabled,
        onQueryChange = onQueryChange,
        onSubmit = { handle(viewModel.submit()) },
        onClose = onClose,
        onTopOnScreen = { barTop = it }
    )

    val ceiling = (settingsBottom ?: statusBarPx) + gapPx
    val maxHeight = with(density) { ((barTop ?: 0) - ceiling - gapPx).coerceAtLeast(0).toDp() }

    val top = barTop ?: return
    val offsetY = displayHeightPx - top + gapPx
    val idle = text.isBlank()
    val frequentApps = if (idle) {
        results.sections.firstOrNull { it.kind == SectionKind.FREQUENT }?.hits?.filterIsInstance<AppHit>().orEmpty()
    } else {
        emptyList()
    }

    FrequentAppsWindow(
        apps = frequentApps,
        loading = idle && results.loading && frequentApps.isEmpty(),
        offsetY = offsetY,
        blurEnabled = blurEnabled,
        icons = viewModel.icons,
        callbacks = callbacks,
        onClose = onClose,
        onHeight = { frequentHeight = it }
    )

    ResultsWindow(
        results = if (idle) SearchResults(loading = false) else results.copy(sections = results.sections.filterNot { it.kind == SectionKind.FREQUENT }),
        offsetY = offsetY,
        maxHeight = maxHeight,
        blurEnabled = blurEnabled,
        icons = viewModel.icons,
        callbacks = callbacks,
        onClose = onClose
    )
}
