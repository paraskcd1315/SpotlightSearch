package com.paraskcd.spotlightsearch

import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.paraskcd.spotlightsearch.ui.components.SearchScreen
import com.paraskcd.spotlightsearch.ui.theme.SpotlightSearchTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchViewModel: SearchViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(100)
                isVisible = true
            }
            SpotlightSearchTheme {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(400)) +
                            scaleIn(initialScale = 0.95f, animationSpec = tween(400, easing = FastOutSlowInEasing))
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        SearchScreen(viewModel = searchViewModel)
                    }
                }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.insetsController?.apply {
            show(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.setBackgroundBlurRadius(75)
        window.setDimAmount(0.5f)
    }
}