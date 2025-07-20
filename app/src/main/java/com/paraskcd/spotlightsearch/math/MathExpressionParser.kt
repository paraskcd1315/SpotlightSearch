package com.paraskcd.spotlightsearch.math

import com.paraskcd.spotlightsearch.types.SearchResult
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.icons.Calculate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MathExpressionParser @Inject constructor() {
    fun parse(raw: String): SearchResult? {
        return try {
            val result = ExpressionEvaluator.evaluate(raw)

            if (result?.isFinite() == true) {
                SearchResult(
                    title = result.toString(),
                    subtitle = "Calculation",
                    onClick = {},
                    iconVector = Calculate,
                    searchResultType = SearchResultType.CALCULATOR
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}