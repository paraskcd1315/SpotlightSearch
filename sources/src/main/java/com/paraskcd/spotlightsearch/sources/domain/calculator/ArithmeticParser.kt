package com.paraskcd.spotlightsearch.sources.domain.calculator

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit

internal class ArithmeticParser {
    fun parse(raw: String): CalculationHit? {
        val result = ExpressionEvaluator.evaluate(raw) ?: return null
        if (!result.isFinite()) return null
        return CalculationHit(answer = result.toString(), kind = CalculationKind.ARITHMETIC)
    }
}
