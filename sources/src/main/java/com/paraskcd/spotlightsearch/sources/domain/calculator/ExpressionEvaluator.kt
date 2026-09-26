package com.paraskcd.spotlightsearch.sources.domain.calculator

internal object ExpressionEvaluator {
    private val contextualRules = listOf(
        Regex("(\\d+)\\s*[x×]\\s*(\\d+)") to "$1 * $2",
        Regex("(\\d+)[x×](\\d+)") to "$1 * $2",
        Regex("(\\d+)\\s*÷\\s*(\\d+)") to "$1 / $2",
        Regex("(\\d+)÷(\\d+)") to "$1 / $2"
    )

    private val normalizingRules = listOf(
        Regex("what is|qué es|cuánto es|cuanto es|calculate|calcula|es|equals|equal to|igual a|=|result of") to "",
        Regex("el doble de\\s*") to "2 * ",
        Regex("la mitad de\\s*") to "0.5 * ",
        Regex("square root of|raíz cuadrada de|sqrt") to "sqrt",
        Regex("cubic root of|raíz cúbica de|cbrt") to "cbrt",
        Regex("(\\d+)\\s*(\\^|\\*\\*)\\s*(\\d+)") to "pow($1,$3)",
        Regex("(\\d+)\\s*(e|×10\\^?)\\s*(-?\\d+)") to "($1 * pow(10,$3))",
        Regex("(\\d+(\\.\\d+)?)\\s*% de\\s*(\\d+(\\.\\d+)?)") to "($1/100)*$3",
        Regex("\\b(pi|π)\\b") to Math.PI.toString(),
        Regex("\\be\\b") to Math.E.toString(),
        Regex("veces|times|x|por") to "*",
        Regex("más|plus|add|suma") to "+",
        Regex("menos|minus|subtract|resta") to "-",
        Regex("entre|divided by|over|divide|dividir") to "/"
    )

    fun evaluate(raw: String): Double? {
        val contextual = contextualRules.fold(raw) { text, (pattern, replacement) -> pattern.replace(text, replacement) }
        val normalized = normalizingRules.fold(contextual) { text, (pattern, replacement) -> pattern.replace(text, replacement) }

        return try {
            val result = PostfixExpressionParser().parse(normalized)
            if (result % 1.0 == 0.0) result.toInt().toDouble() else result
        } catch (_: Exception) {
            null
        }
    }
}
