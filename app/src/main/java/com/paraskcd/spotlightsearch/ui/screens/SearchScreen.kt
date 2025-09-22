package com.paraskcd.spotlightsearch.ui.screens

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import kotlinx.coroutines.flow.debounce
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import com.paraskcd.spotlightsearch.SearchViewModel
import kotlinx.coroutines.FlowPreview
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.paraskcd.spotlightsearch.SettingsActivity
import com.paraskcd.spotlightsearch.ui.components.SearchBar
import com.paraskcd.spotlightsearch.ui.components.SearchResultList
import kotlinx.coroutines.android.awaitFrame

@OptIn(FlowPreview::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, supportsBlur: Boolean) {
    var localQuery by remember { mutableStateOf("") }
    val results by viewModel.results.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val iconTintColorArgb = MaterialTheme.colorScheme.primary.toArgb()

    DisposableEffect(lifecycleOwner) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                focusManager.clearFocus()
                keyboardController?.hide()
                activity?.finish()
            }
        }

        val onBackPressedDispatcher = (activity as? ComponentActivity)?.onBackPressedDispatcher
        onBackPressedDispatcher?.addCallback(lifecycleOwner, callback)

        onDispose {
            callback.remove()
        }
    }

    LaunchedEffect(Unit) {
        awaitFrame()
        awaitFrame()
        focusRequester.requestFocus()
        awaitFrame()
        awaitFrame()
    }

    LaunchedEffect(localQuery) {
        snapshotFlow { localQuery }
            .debounce(400)
            .collect { debouncedText ->
                viewModel.onQueryChanged(debouncedText)
            }
    }

    val isImeVisible = imeBottom > 0.dp
    val reduceFactor = 0.85f
    val minGap = 8.dp

    val bottomPadding = if (isImeVisible) {
        (imeBottom * (1 - reduceFactor)).coerceAtLeast(minGap)
    } else {
        navBottom
    }

    Scaffold (
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = bottomPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    query = localQuery,
                    onQueryChanged = { localQuery = it },
                    onClear = {
                        localQuery = ""
                    },
                    focusRequester = focusRequester,
                    onSearchImeAction = {
                        viewModel.onSearch(localQuery)
                        activity?.finish()
                    },
                    supportsBlur = supportsBlur,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    onClick = {
                        activity?.startActivity(Intent(activity, SettingsActivity::class.java))
                    },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (supportsBlur) 0.65f else 1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(8.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                        )
                    }
                }

                Surface(
                    onClick = {
                        activity?.finish()
                    },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (supportsBlur) 0.65f else 1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(8.dp)
                            .size(40.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Exit",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                        )
                    }
                }
            }
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            SearchResultList(
                results = results,
                onQueryChanged = { localQuery = it },
                supportsBlur = supportsBlur,
                resolveThemedAppIcon = { pkg ->
                    viewModel.getAppIcons(pkg, dynamicColor = iconTintColorArgb)
                }
            )
        }
    }
}