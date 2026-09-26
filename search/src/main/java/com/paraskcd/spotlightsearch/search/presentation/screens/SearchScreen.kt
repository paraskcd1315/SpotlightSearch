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
import androidx.compose.ui.unit.IntSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.search.infrastructure.window.DialogWindowSetup
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.HitOutcome
import com.paraskcd.spotlightsearch.search.presentation.overlay.FilterWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.OverlayMetrics
import com.paraskcd.spotlightsearch.search.presentation.overlay.OverlayScrim
import com.paraskcd.spotlightsearch.search.presentation.overlay.ResultsWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.SearchBarWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.SettingsButtonWindow
import com.paraskcd.spotlightsearch.search.presentation.overlay.FrequentAppsWindow
import com.paraskcd.spotlightsearch.search.presentation.model.SearchResults
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.search.presentation.utils.filterKinds
import com.paraskcd.spotlightsearch.search.presentation.utils.filteredBy
import com.paraskcd.spotlightsearch.search.presentation.viewmodels.SearchViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    blurEnabled: Boolean,
    showBranding: Boolean,
    appName: String,
    onOpenSettings: () -> Unit,
    onClose: () -> Unit
) {
    val results by viewModel.results.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var barTop by remember { mutableStateOf<Int?>(null) }
    var settingsSize by remember { mutableStateOf<IntSize?>(null) }
    var filterHeight by remember { mutableStateOf(0) }
    var filter by remember { mutableStateOf<SectionKind?>(null) }
    var frequentHeight by remember { mutableStateOf<Int?>(null) }
    var keyboardSettled by remember { mutableStateOf(false) }
    val view = LocalView.current
    val density = LocalDensity.current
    val activityWindow = LocalActivity.current?.window
    val displayHeightPx = activityWindow?.let(DialogWindowSetup::displayHeight) ?: 0
    val displayWidthPx = activityWindow?.let(DialogWindowSetup::displayWidth) ?: 0
    val sideMarginPx = (displayWidthPx * (1f - OverlayMetrics.WindowWidthFraction) / 2f).roundToInt()
    val statusBarPx = WindowInsets.statusBars.getTop(density)
    val gapPx = with(density) { OverlayMetrics.ResultsGap.roundToPx() }
    val toolbarPx = settingsSize?.height ?: 0

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
        delay(OverlayMetrics.KeyboardSettleTimeoutMs)
        keyboardSettled = true
    }

    val frequentReserve by animateIntAsState(
        targetValue = frequentHeight?.let { it + gapPx } ?: 0,
        animationSpec = tween(SpMotion.durAutoHeightMs, easing = SpMotion.easeIos)
    )

    OverlayScrim(
        visible = visible && keyboardSettled && barTop != null && settingsSize != null,
        showBranding = showBranding,
        appName = appName,
        icons = viewModel.icons.apps,
        topLimitPx = statusBarPx,
        bottomLimitPx = barTop?.let { it - gapPx - toolbarPx - frequentReserve },
        onClose = onClose
    )

    if (!visible) return

    SearchBarWindow(
        query = text,
        blurEnabled = blurEnabled,
        onQueryChange = onQueryChange,
        onSubmit = { handle(viewModel.submit()) },
        onClose = onClose,
        onTopOnScreen = { barTop = it },
        onKeyboardShown = { keyboardSettled = true }
    )

    val top = barTop ?: return
    val toolbarOffsetY = displayHeightPx - top + gapPx
    val panelOffsetY = toolbarOffsetY + toolbarPx + gapPx
    val ceiling = statusBarPx + gapPx
    val panelHeightPx = (top - gapPx - toolbarPx - gapPx - ceiling).coerceAtLeast(0)
    val idle = text.isBlank()
    LaunchedEffect(idle) { if (idle) filter = null }

    SettingsButtonWindow(
        visible = keyboardSettled,
        blurEnabled = blurEnabled,
        offsetX = sideMarginPx,
        offsetY = toolbarOffsetY,
        onOpenSettings = onOpenSettings,
        onClose = onClose,
        onSize = { settingsSize = it }
    )

    val sections = if (idle) emptyList() else results.sections.filterNot { it.kind == SectionKind.FREQUENT }
    val kinds = sections.filterKinds()
    val active = filter?.takeIf { it in kinds }
    val filterMaxWidthPx = displayWidthPx - 2 * sideMarginPx - (settingsSize?.width ?: 0) - gapPx

    FilterWindow(
        kinds = kinds,
        active = active,
        onSelect = { filter = it },
        offsetX = sideMarginPx,
        offsetY = toolbarOffsetY + ((toolbarPx - filterHeight) / 2).coerceAtLeast(0),
        maxWidth = with(density) { filterMaxWidthPx.coerceAtLeast(0).toDp() },
        blurEnabled = blurEnabled,
        onClose = onClose,
        onHeight = { filterHeight = it }
    )

    val toolbarReady = keyboardSettled && settingsSize != null
    val frequentApps = if (idle && toolbarReady) {
        results.sections.firstOrNull { it.kind == SectionKind.FREQUENT }?.hits?.filterIsInstance<AppHit>().orEmpty()
    } else {
        emptyList()
    }

    FrequentAppsWindow(
        apps = frequentApps,
        loading = idle && toolbarReady && results.loading && frequentApps.isEmpty(),
        offsetY = panelOffsetY,
        blurEnabled = blurEnabled,
        icons = viewModel.icons,
        callbacks = callbacks,
        onClose = onClose,
        onHeight = { frequentHeight = it }
    )

    ResultsWindow(
        results = if (idle) {
            SearchResults(loading = false)
        } else {
            results.copy(sections = sections.filteredBy(active))
        },
        offsetY = panelOffsetY,
        maxHeightPx = panelHeightPx,
        blurEnabled = blurEnabled,
        icons = viewModel.icons,
        callbacks = callbacks,
        onClose = onClose
    )
}
