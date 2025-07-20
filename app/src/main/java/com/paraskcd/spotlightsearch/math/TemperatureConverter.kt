package com.paraskcd.spotlightsearch.math

import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.icons.Calculate
import com.paraskcd.spotlightsearch.types.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemperatureConverter @Inject constructor() {
    fun parse(raw: String): SearchResult? {
        val pattern = Regex("""^(-?\d+(?:\.\d+)?)\s*(c|celsius|°c)\s*(a|to|en|in)\s*(f|fahrenheit|°f)$""", RegexOption.IGNORE_CASE)
        val patternReverse = Regex("""^(-?\d+(?:\.\d+)?)\s*(f|fahrenheit|°f)\s*(a|to|en|in)\s*(c|celsius|°c)$""", RegexOption.IGNORE_CASE)

        val match = pattern.matchEntire(raw) ?: patternReverse.matchEntire(raw)
        if (match != null) {
            val value = match.groupValues[1].toDoubleOrNull() ?: return null
            val from = match.groupValues[2].lowercase()
            val to = match.groupValues[4].lowercase()

            val (converted, unit) = when {
                from.startsWith("c") && to.startsWith("f") -> (value * 9 / 5 + 32) to "°F"
                from.startsWith("f") && to.startsWith("c") -> ((value - 32) * 5 / 9) to "°C"
                else -> return null
            }

            return SearchResult(
                title = "%.2f %s".format(converted, unit),
                subtitle = "Temperature conversion",
                iconVector = Calculate,
                searchResultType = SearchResultType.CALCULATOR,
                onClick = {}
            )
        }

        return null
    }
}