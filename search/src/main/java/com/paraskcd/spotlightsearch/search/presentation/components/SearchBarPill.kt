package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@Composable
fun SearchBarPill(
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SearchMetrics.PillPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
            },
            trailingIcon = {
                AnimatedVisibility(visible = query.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(SearchMetrics.ClearButtonSize)) {
                        Box(
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                                CircleShape
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.search_clear),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(SearchMetrics.ClearIconSize)
                            )
                        }
                    }
                }
            }
        )
    }
}
