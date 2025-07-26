package com.paraskcd.spotlightsearch.ui.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import com.paraskcd.spotlightsearch.SearchViewModel
import kotlinx.coroutines.FlowPreview
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.paraskcd.spotlightsearch.ui.components.CloseButton
import com.paraskcd.spotlightsearch.ui.components.SearchBar
import com.paraskcd.spotlightsearch.ui.components.SearchResultList

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    var localQuery by remember { mutableStateOf("") }
    val results by viewModel.results.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(lifecycleOwner) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Cierra teclado y focus primero
                focusManager.clearFocus()
                keyboardController?.hide()
                // Luego termina la activity
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
        focusRequester.requestFocus()
    }

    LaunchedEffect(localQuery) {
        snapshotFlow { localQuery }
            .debounce(400)
            .collect { debouncedText ->
                viewModel.onQueryChanged(debouncedText)
            }
    }

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically { fullHeight -> fullHeight } + fadeIn()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    WindowInsets.safeDrawing
                        .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                        .asPaddingValues()
                )
                .padding(horizontal = 16.dp)
        ) {
            CloseButton {
                activity?.finish()
            }

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
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            SearchResultList(
                results = results,
                onQueryChanged = { localQuery = it }
            )
        }
    }
}