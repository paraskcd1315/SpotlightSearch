package com.paraskcd.spotlightsearch.ui.components

import androidx.activity.compose.BackHandler
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

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    var localQuery by remember { mutableStateOf("") }
    val results by viewModel.results.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(localQuery) {
        snapshotFlow { localQuery }
            .debounce(300)
            .collect { debouncedText ->
                viewModel.onQueryChanged(debouncedText)
            }
    }

    BackHandler {
        activity?.finish()
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
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                focusRequester = focusRequester
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            SearchResultList(
                results = results
            )
        }
    }
}