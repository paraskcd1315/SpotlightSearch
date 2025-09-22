package com.paraskcd.spotlightsearch.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.types.SearchResult
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.ui.modifiers.drawFadingEdgesBasic

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchResultList(
    results: List<SearchResult>,
    onQueryChanged: (String) -> Unit,
    supportsBlur: Boolean,
    resolveThemedAppIcon: ((String) -> Drawable?)?
) {
    val grouped = remember(results) {
        val sections = mutableListOf<List<SearchResult>>()
        var currentSection = mutableListOf<SearchResult>()

        for (result in results) {
            if (result.isHeader) {
                if (currentSection.isNotEmpty()) sections.add(currentSection)
                currentSection = mutableListOf(result)
            } else {
                currentSection.add(result)
            }
        }
        if (currentSection.isNotEmpty()) sections.add(currentSection)
        sections
    }

    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val reduceFactor = 0.1f
    val minGap = 2.dp
    val adjustedImeBottom = if (imeBottom > minGap) (imeBottom * (1 - reduceFactor)).coerceAtLeast(0.dp) else imeBottom
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().drawFadingEdgesBasic(scrollableState),
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 8.dp),
        state = scrollableState
    ) {
        grouped.forEachIndexed { index, section ->
            val header = section.firstOrNull()
            val body = section.drop(1)
            val isFrequentBlock = header?.title == "Frequently used" &&
                    body.all { it.searchResultType == SearchResultType.APP_FREQUENT }

            item {
                Column {
                    section.firstOrNull()?.let { SearchResultItem(it, onQueryChanged, resolveThemedAppIcon) }
                    if (isFrequentBlock) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (supportsBlur) 0.65f else 1f),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .padding(vertical = 1.dp)
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, bottom = 8.dp),
                                maxItemsInEachRow = 5,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                body.forEach { item ->
                                    SearchResultItem(item, onQueryChanged, resolveThemedAppIcon)
                                }
                            }
                        }

                    } else {
                        val itemCount = body.size

                        body.forEachIndexed { i, item ->
                            val shape = when {
                                itemCount == 1 -> RoundedCornerShape(24.dp)
                                i == 0 -> RoundedCornerShape(
                                    topStart = 24.dp, topEnd = 24.dp,
                                    bottomStart = 8.dp, bottomEnd = 8.dp
                                )
                                i == itemCount - 1 -> RoundedCornerShape(
                                    topStart = 8.dp, topEnd = 8.dp,
                                    bottomStart = 24.dp, bottomEnd = 24.dp
                                )
                                else -> RoundedCornerShape(8.dp)
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = if (supportsBlur) 0.65f else 1f),
                                shape = shape,
                                modifier = Modifier
                                    .padding(vertical = 1.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                        shape = shape
                                    )
                            ) {
                                SearchResultItem(item, onQueryChanged, resolveThemedAppIcon)
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(adjustedImeBottom))
        }
    }
}