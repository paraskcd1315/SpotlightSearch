package com.paraskcd.spotlightsearch.sources.domain.calculator

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit

internal class UnitConverter {
    private val units =
        "g|grams?|gramos?|kg|kilograms?|kilogramos?|m|meters?|metros?|cm|centimeters?|centímetros?|mm|milimeters?|" +
            "milímetros?|in|inch|inches|pulgadas?|ft|feet|foot|pies?|pie|km|kilometers?|kilómetros?|miles?|millas?|" +
            "yardas?|yards?|nm|nanometers?|nanómetros?|nautical miles?|millas náuticas?|light[-\\s]?years?|años luz|" +
            "l|liters?|litros?|gal|gallons?|galones?"
    private val pattern = Regex("""^(\d+(?:\.\d+)?)\s*($units)\s*(a|en|to|in)\s*($units)$""", RegexOption.IGNORE_CASE)

    fun parse(raw: String): CalculationHit? {
        val match = pattern.matchEntire(raw) ?: return null
        val value = match.groupValues[1].toDoubleOrNull() ?: return null
        val from = match.groupValues[2].lowercase()
        val to = match.groupValues[4].lowercase()
        val (answer, detail) = convert(value, from, to) ?: return null
        return CalculationHit(answer = answer, kind = CalculationKind.UNIT, detail = detail)
    }

    private fun convert(value: Double, from: String, to: String): Pair<String, String?>? = when {
        from.contains("kg") && to.contains("pound") -> {
            val detail = when {
                value < 1 -> "${(value * 1000).toInt()} g"
                value >= 1000 -> "%.2f tons".format(value / 1000)
                else -> null
            }
            "%.2f lbs".format(value * 2.20462) to detail
        }
        from.contains("km") && to.contains("mile") -> "%.2f miles".format(value * 0.621371) to null
        from.contains("l") && to.contains("gallon") -> "%.2f gal".format(value * 0.264172) to null
        from.contains("cm") && to.contains("inch") -> {
            val inches = value * 0.393701
            "%.2f in".format(inches) to (inches / 12).takeIf { it >= 1 }?.let { "%.2f ft".format(it) }
        }
        from == "m" && to.contains("ft") -> {
            val feet = value * 3.28084
            "%.2f ft".format(feet) to feet.takeIf { it < 1 }?.let { "%.2f in".format(it * 12) }
        }
        from.contains("mm") && to.contains("inch") -> "%.2f in".format(value * 0.0393701) to null
        from.contains("yard") && to == "m" -> "%.2f m".format(value * 0.9144) to null
        from.contains("nm") && to == "m" -> "%.2e m".format(value * 1e-9) to null
        from.contains("nautical") && to.contains("km") -> "%.2f km".format(value * 1.852) to null
        from.contains("light") && to.contains("km") -> "%.2e km".format(value * 9.461e12) to null
        from.contains("in") && to.contains("ft") -> {
            val feet = value / 12.0
            "%.2f ft".format(feet) to feet.takeIf { it < 1 }?.let { "%.2f in".format(value) }
        }
        from.contains("cm") && to == "m" -> "%.2f m".format(value / 100.0) to null
        from.contains("in") && to == "m" -> "%.3f m".format(value * 0.0254) to null
        from.contains("ft") && to == "m" -> "%.3f m".format(value * 0.3048) to null
        from.contains("mile") && to.contains("km") -> "%.2f km".format(value * 1.60934) to null
        from == "g" && to == "kg" -> "%.3f kg".format(value / 1000.0) to null
        from == "kg" && to == "g" -> "%.0f g".format(value * 1000.0) to null
        else -> null
    }
}
