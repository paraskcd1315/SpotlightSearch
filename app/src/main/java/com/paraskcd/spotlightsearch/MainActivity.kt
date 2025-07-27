package com.paraskcd.spotlightsearch

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.paraskcd.spotlightsearch.ui.screens.SearchScreen
import com.paraskcd.spotlightsearch.ui.theme.SpotlightSearchTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalView
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.graphicsLayer

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchViewModel: SearchViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            var isVisible by remember { mutableStateOf(false) }
            val isBatterySaver = remember {
                isBatterySaverOn(this)
            }
            val localView = LocalView.current
            var dragOffsetY by remember { mutableStateOf(0f) }

            // Threshold at which the search screen content becomes fully transparent
            val alphaThresholdDragY = 300f
            // Point at which blur should be fully gone after alpha is zero
            // Make this larger than alphaThresholdDragY to slow down the blur removal
            val blurFullyGoneDragY = 450f // Or any value > alphaThresholdDragY

            // Animate dragOffsetY for smoother release
            val animatedDragOffsetY by animateFloatAsState(
                targetValue = dragOffsetY,
                animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing), // Adjust timing as needed
                label = "animatedDragOffsetY"
            )

            LaunchedEffect(Unit) {
                delay(250)
                localView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                isVisible = true
                // Set initial blur when activity starts
                window.setBackgroundBlurRadius(100)
            }

            SpotlightSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (isBatterySaver) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.background.copy(alpha = (0.5f - (animatedDragOffsetY / (alphaThresholdDragY * 2))).coerceIn(0f, 0.5f)),
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .offset { IntOffset(0, animatedDragOffsetY.roundToInt()) }
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onVerticalDrag = { change, dragAmount ->
                                        dragOffsetY += dragAmount

                                        val currentBlur = if (dragOffsetY < alphaThresholdDragY) {
                                            (100f * (1f - (dragOffsetY / alphaThresholdDragY))).coerceIn(0f, 100f)
                                        } else {
                                            val remainingDragForBlur = blurFullyGoneDragY - alphaThresholdDragY
                                            if (remainingDragForBlur <= 0) {
                                                0f
                                            } else {
                                                val progressAfterAlphaZero = (dragOffsetY - alphaThresholdDragY) / (blurFullyGoneDragY - alphaThresholdDragY)
                                                (0f * (1f - progressAfterAlphaZero.coerceIn(0f, 1f)))
                                                val blurAtAlphaZero = 20f
                                                val blurStage1 = 100f - ( (100f - blurAtAlphaZero) * (dragOffsetY / alphaThresholdDragY) )

                                                if (dragOffsetY < alphaThresholdDragY) {
                                                    blurStage1.coerceIn(blurAtAlphaZero, 100f)
                                                } else {
                                                    val progressInStage2 = (dragOffsetY - alphaThresholdDragY) / (blurFullyGoneDragY - alphaThresholdDragY)
                                                    (blurAtAlphaZero * (1f - progressInStage2)).coerceIn(0f, blurAtAlphaZero)
                                                }
                                            }
                                        }
                                        window.setBackgroundBlurRadius(currentBlur.roundToInt())
                                        change.consume()
                                    },
                                    onDragEnd = {
                                        if (animatedDragOffsetY > 100f) {
                                            finish()
                                        } else {
                                            dragOffsetY = 0f
                                        }
                                    }
                                )
                            }
                    ) {
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(tween(400)) +
                                    scaleIn(initialScale = 0.95f, animationSpec = tween(400, easing = FastOutSlowInEasing)),
                            modifier = Modifier.graphicsLayer {
                                alpha = (1f - (dragOffsetY / 300f)).coerceIn(0f, 1f)
                            }
                        ) {
                            SearchScreen(viewModel = searchViewModel)
                        }
                    }
                }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.insetsController?.apply {
            show(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.setBackgroundBlurRadius(100) // Now handled reactively
        window.setDimAmount(0.0f)
    }

    private fun isBatterySaverOn(context: Context): Boolean {
        val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        return powerManager.isPowerSaveMode
    }
}