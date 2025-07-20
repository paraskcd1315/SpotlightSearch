package com.paraskcd.spotlightsearch.math

import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.icons.Calculate
import com.paraskcd.spotlightsearch.types.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnitConverter @Inject constructor() {
    fun parse(raw: String): SearchResult? {
        val pattern = Regex(
            """^(\d+(?:\.\d+)?)\s*(g|grams?|gramos?|kg|kilograms?|kilogramos?|m|meters?|metros?|cm|centimeters?|centímetros?|mm|milimeters?|milímetros?|in|inch|inches|pulgadas?|ft|feet|foot|pies?|pie|km|kilometers?|kilómetros?|miles?|millas?|yardas?|yards?|nm|nanometers?|nanómetros?|nautical miles?|millas náuticas?|light[-\s]?years?|años luz|l|liters?|litros?|gal|gallons?|galones?)\s*(a|en|to|in)\s*(g|grams?|gramos?|kg|kilograms?|kilogramos?|m|meters?|metros?|cm|centimeters?|centímetros?|mm|milimeters?|milímetros?|in|inch|inches|pulgadas?|ft|feet|foot|pies?|pie|km|kilometers?|kilómetros?|miles?|millas?|yardas?|yards?|nm|nanometers?|nanómetros?|nautical miles?|millas náuticas?|light[-\s]?years?|años luz|l|liters?|litros?|gal|gallons?|galones?)$""",
            RegexOption.IGNORE_CASE
        )
        val match = pattern.matchEntire(raw) ?: return null

        val value = match.groupValues[1].toDoubleOrNull() ?: return null
        val from = match.groupValues[2].lowercase()
        val to = match.groupValues[4].lowercase()

        val (converted, unit, human, alt) = when {
            from.contains("kg") && to.contains("pound") -> {
                val conv = value * 2.20462
                val alt = if (value < 1) "${(value * 1000).toInt()} g" else if (value >= 1000) "%.2f tons".format(value / 1000) else null
                Quad(conv, "lbs", "%.2f lbs".format(conv), alt)
            }
            from.contains("km") && to.contains("mile") -> {
                val conv = value * 0.621371
                Quad(conv, "miles", "%.2f miles".format(conv), null)
            }
            from.contains("l") && to.contains("gallon") -> {
                val conv = value * 0.264172
                Quad(conv, "gal", "%.2f gal".format(conv), null)
            }
            from.contains("cm") && to.contains("inch") -> {
                val conv = value * 0.393701
                val feet = conv / 12
                val alt = if (feet >= 1) "%.2f ft".format(feet) else null
                Quad(conv, "in", "%.2f in".format(conv), alt)
            }
            from == "m" && to.contains("ft") -> {
                val conv = value * 3.28084
                val alt = if (conv < 1) "%.2f in".format(conv * 12) else null
                Quad(conv, "ft", "%.2f ft".format(conv), alt)
            }
            from.contains("mm") && to.contains("inch") -> {
                val conv = value * 0.0393701
                Quad(conv, "in", "%.2f in".format(conv), null)
            }
            from.contains("yard") && to == "m" -> {
                val conv = value * 0.9144
                Quad(conv, "m", "%.2f m".format(conv), null)
            }
            from.contains("nm") && to == "m" -> {
                val conv = value * 1e-9
                Quad(conv, "m", "%.2e m".format(conv), null)
            }
            from.contains("nautical") && to.contains("km") -> {
                val conv = value * 1.852
                Quad(conv, "km", "%.2f km".format(conv), null)
            }
            from.contains("light") && to.contains("km") -> {
                val conv = value * 9.461e12
                Quad(conv, "km", "%.2e km".format(conv), null)
            }
            from.contains("in") && to.contains("ft") -> {
                val conv = value / 12.0
                val alt = if (conv < 1) "%.2f in".format(value) else null
                Quad(conv, "ft", "%.2f ft".format(conv), alt)
            }

            from.contains("cm") && to == "m" -> {
                val conv = value / 100.0
                Quad(conv, "m", "%.2f m".format(conv), null)
            }

            from.contains("in") && to == "m" -> {
                val conv = value * 0.0254
                Quad(conv, "m", "%.3f m".format(conv), null)
            }

            from.contains("ft") && to == "m" -> {
                val conv = value * 0.3048
                Quad(conv, "m", "%.3f m".format(conv), null)
            }

            from.contains("mile") && to.contains("km") -> {
                val conv = value * 1.60934
                Quad(conv, "km", "%.2f km".format(conv), null)
            }

            from == "g" && to == "kg" -> {
                val conv = value / 1000.0
                Quad(conv, "kg", "%.3f kg".format(conv), null)
            }

            from == "kg" && to == "g" -> {
                val conv = value * 1000.0
                Quad(conv, "g", "%.0f g".format(conv), null)
            }
            else -> return null
        }

        return SearchResult(
            title = human,
            subtitle = alt ?: "Unit conversion",
            iconVector = Calculate,
            searchResultType = SearchResultType.CALCULATOR,
            onClick = {}
        )
    }

    private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}