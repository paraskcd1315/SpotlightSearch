package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

fun highlightedTitle(title: String, matches: List<IntRange>, color: Color): AnnotatedString = buildAnnotatedString {
    append(title)
    val style = SpanStyle(color = color, fontWeight = FontWeight.Bold)
    matches
        .filter { it.first >= 0 && it.last < title.length && !it.isEmpty() }
        .forEach { addStyle(style, it.first, it.last + 1) }
}
