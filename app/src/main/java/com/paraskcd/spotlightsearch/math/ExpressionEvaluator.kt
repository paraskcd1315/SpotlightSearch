package com.paraskcd.spotlightsearch.math

object ExpressionEvaluator {
    fun evaluate(raw: String): Double? {
        val contextual = raw
            .replace(Regex("(\\d+)\\s*[x×]\\s*(\\d+)"), "$1 * $2")
            .replace(Regex("(\\d+)[x×](\\d+)"), "$1 * $2")
            .replace(Regex("(\\d+)\\s*÷\\s*(\\d+)"), "$1 / $2")
            .replace(Regex("(\\d+)÷(\\d+)"), "$1 / $2")

        val normalized = contextual
            .replace(Regex("what is|qué es|cuánto es|cuanto es|calculate|calcula|es|equals|equal to|igual a|=|result of"), "")
            .replace(Regex("el doble de\\s*"), "2 * ")
            .replace(Regex("la mitad de\\s*"), "0.5 * ")
            .replace(Regex("square root of|raíz cuadrada de|sqrt"), "sqrt")
            .replace(Regex("cubic root of|raíz cúbica de|cbrt"), "cbrt")
            .replace(Regex("(\\d+)\\s*(\\^|\\*\\*)\\s*(\\d+)"), "pow($1,$3)")
            .replace(Regex("(\\d+)\\s*(e|×10\\^?)\\s*(-?\\d+)"), "($1 * pow(10,$3))")
            .replace(Regex("(\\d+(\\.\\d+)?)\\s*% de\\s*(\\d+(\\.\\d+)?)"), "($1/100)*$3")
            .replace(Regex("\\b(pi|π)\\b"), Math.PI.toString())
            .replace(Regex("\\be\\b"), Math.E.toString())
            .replace(Regex("veces|times|x|por"), "*")
            .replace(Regex("más|plus|add|suma"), "+")
            .replace(Regex("menos|minus|subtract|resta"), "-")
            .replace(Regex("entre|divided by|over|divide|dividir"), "/")

        return try {
            val result = KotlinExpressionParser().parse(normalized)
            if (result % 1.0 == 0.0) result.toInt().toDouble() else result
        } catch (e: Exception) {
            null
        }
    }
}