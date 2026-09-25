package com.paraskcd.spotlightsearch.sources.domain.calculator

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit

internal class TemperatureConverter {
    private val celsiusToFahrenheit =
        Regex("""^(-?\d+(?:\.\d+)?)\s*(c|celsius|°c)\s*(a|to|en|in)\s*(f|fahrenheit|°f)$""", RegexOption.IGNORE_CASE)
    private val fahrenheitToCelsius =
        Regex("""^(-?\d+(?:\.\d+)?)\s*(f|fahrenheit|°f)\s*(a|to|en|in)\s*(c|celsius|°c)$""", RegexOption.IGNORE_CASE)

    fun parse(raw: String): CalculationHit? {
        celsiusToFahrenheit.matchEntire(raw)?.let { match ->
            val value = match.groupValues[1].toDoubleOrNull() ?: return null
            return hit(value * 9 / 5 + 32, "°F")
        }
        fahrenheitToCelsius.matchEntire(raw)?.let { match ->
            val value = match.groupValues[1].toDoubleOrNull() ?: return null
            return hit((value - 32) * 5 / 9, "°C")
        }
        return null
    }

    private fun hit(value: Double, unit: String) =
        CalculationHit(answer = "%.2f %s".format(value, unit), kind = CalculationKind.TEMPERATURE)
}
