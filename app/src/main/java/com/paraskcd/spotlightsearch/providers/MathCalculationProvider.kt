package com.paraskcd.spotlightsearch.providers

import com.paraskcd.spotlightsearch.math.DateExpressionParser
import com.paraskcd.spotlightsearch.math.MathExpressionParser
import com.paraskcd.spotlightsearch.math.TemperatureConverter
import com.paraskcd.spotlightsearch.math.UnitConverter
import com.paraskcd.spotlightsearch.types.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MathEvaluationProvider @Inject constructor(
    private val mathParser: MathExpressionParser,
    private val dateParser: DateExpressionParser,
    private val tempConverter: TemperatureConverter,
    private val unitConverter: UnitConverter,
) {
    fun evaluate(input: String): SearchResult? {
        val raw = input.lowercase().trim()

        return dateParser.parse(raw)
            ?: unitConverter.parse(raw)
            ?: tempConverter.parse(raw)
            ?: mathParser.parse(raw)
    }
}