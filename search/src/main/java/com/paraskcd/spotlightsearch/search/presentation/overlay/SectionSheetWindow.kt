package com.paraskcd.spotlightsearch.search.presentation.overlay

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.paraskcd.spotlightsearch.designsystem.signature.organisms.SpBottomSheet
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpMotion
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.infrastructure.window.DialogWindowSetup
import com.paraskcd.spotlightsearch.search.presentation.components.GlassRow
import com.paraskcd.spotlightsearch.search.presentation.components.HitContent
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.titleRes
import kotlinx.coroutines.delay

@Composable
fun SectionSheetWindow(
    section: SearchSection?,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    onDismiss: () -> Unit
) {
    var retained by remember { mutableStateOf(section) }
    var open by remember { mutableStateOf(false) }
    LaunchedEffect(section) {
        if (section != null) {
            retained = section
            open = true
        } else {
            open = false
            delay(SpMotion.durPushMs.toLong())
            retained = null
        }
    }
    val current = retained ?: return
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        val window = (LocalView.current.parent as DialogWindowProvider).window
        val blur = remember { Animatable(0f) }
        SideEffect {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setDimAmount(0f)
            window.addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            }
        }
        LaunchedEffect(open, blurEnabled) {
            val target = if (open && blurEnabled) OverlayMetrics.BlurRadiusMax.toFloat() else 0f
            blur.animateTo(target, tween(SpMotion.durPushMs, easing = SpMotion.easeIos)) {
                DialogWindowSetup.setBlur(window, value.toInt())
            }
        }
        val maxListHeight = with(LocalDensity.current) {
            (LocalWindowInfo.current.containerSize.height * SearchMetrics.SheetListHeightFraction).toDp()
        }
        SpBottomSheet(visible = open, onDismiss = onDismiss, title = stringResource(current.kind.titleRes())) {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = maxListHeight)) {
                itemsIndexed(current.hits, contentType = { _, hit -> hit::class }) { index, hit ->
                    GlassRow(index = index, count = current.hits.size, blurEnabled = blurEnabled) {
                        HitContent(hit, icons, callbacks)
                    }
                }
            }
        }
    }
}
