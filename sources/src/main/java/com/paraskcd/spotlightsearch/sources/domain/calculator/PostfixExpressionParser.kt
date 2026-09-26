package com.paraskcd.spotlightsearch.sources.domain.calculator

import java.util.Stack
import kotlin.math.pow

internal class PostfixExpressionParser {
    private val number = Regex("""\d+(\.\d+)?""")
    private val token = Regex("""\d+(\.\d+)?|[()+\-*/^]""")
    private val precedence = mapOf("^" to 4, "*" to 3, "/" to 3, "+" to 2, "-" to 2)

    fun parse(expression: String): Double = evaluatePostfix(infixToPostfix(tokenize(expression)))

    private fun tokenize(expression: String): List<String> =
        token.findAll(expression.replace(" ", "")).map { it.value }.toList()

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operators = Stack<String>()

        for (token in tokens) {
            when {
                token.matches(number) -> output.add(token)
                token == "(" -> operators.push(token)
                token == ")" -> {
                    while (operators.peek() != "(") output.add(operators.pop())
                    operators.pop()
                }
                token in precedence -> {
                    while (operators.isNotEmpty() &&
                        precedence.getOrDefault(operators.peek(), 0) >= precedence.getValue(token)
                    ) {
                        output.add(operators.pop())
                    }
                    operators.push(token)
                }
            }
        }
        while (operators.isNotEmpty()) output.add(operators.pop())
        return output
    }

    private fun evaluatePostfix(tokens: List<String>): Double {
        val stack = Stack<Double>()
        for (token in tokens) {
            if (token.matches(number)) {
                stack.push(token.toDouble())
                continue
            }
            val right = stack.pop()
            val left = stack.pop()
            stack.push(
                when (token) {
                    "+" -> left + right
                    "-" -> left - right
                    "*" -> left * right
                    "/" -> left / right
                    else -> left.pow(right)
                }
            )
        }
        return stack.pop()
    }
}
