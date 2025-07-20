package com.paraskcd.spotlightsearch.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.types.SearchResult
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun SearchResultList(results: List<SearchResult>) {
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

    val sectionVisibility = remember { mutableStateListOf<Boolean>() }

    LaunchedEffect(grouped) {
        sectionVisibility.clear()
        grouped.forEachIndexed { index, _ ->
            delay(100L * index)
            sectionVisibility.add(true)
        }
    }

    LazyColumn {
        grouped.forEachIndexed { index, section ->
            item {
                AnimatedVisibility(
                    visible = sectionVisibility.getOrNull(index) == true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        Column {
                            section.forEach { result ->
                                SearchResultItem(result)
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}