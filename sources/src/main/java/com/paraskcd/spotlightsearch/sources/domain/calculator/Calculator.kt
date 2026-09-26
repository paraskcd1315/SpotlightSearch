package com.paraskcd.spotlightsearch.sources.domain.calculator

import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit
import javax.inject.Inject

class Calculator @Inject constructor() {
    private val dateParser = DateExpressionParser()
    private val unitConverter = UnitConverter()
    private val temperatureConverter = TemperatureConverter()
    private val arithmetic = ArithmeticParser()

    fun evaluate(input: String): CalculationHit? {
        val raw = input.lowercase().trim()
        return dateParser.parse(raw)
            ?: unitConverter.parse(raw)
            ?: temperatureConverter.parse(raw)
            ?: arithmetic.parse(raw)
    }
}
