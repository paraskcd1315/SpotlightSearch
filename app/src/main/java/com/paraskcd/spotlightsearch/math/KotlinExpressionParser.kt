package com.paraskcd.spotlightsearch.math

import java.util.Stack
import kotlin.math.*

class KotlinExpressionParser {

    fun parse(expression: String): Double {
        val tokens = tokenize(expression)
        val postfix = infixToPostfix(tokens)
        return evaluatePostfix(postfix)
    }

    private fun tokenize(expr: String): List<String> {
        val regex = Regex("""\d+(\.\d+)?|[()+\-*/^]""")
        return regex.findAll(expr.replace(" ", "")).map { it.value }.toList()
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val precedence = mapOf("^" to 4, "*" to 3, "/" to 3, "+" to 2, "-" to 2)
        val output = mutableListOf<String>()
        val ops = Stack<String>()

        for (token in tokens) {
            when {
                token.matches(Regex("""\d+(\.\d+)?""")) -> output.add(token)
                token == "(" -> ops.push(token)
                token == ")" -> {
                    while (ops.peek() != "(") output.add(ops.pop())
                    ops.pop()
                }
                token in precedence -> {
                    while (ops.isNotEmpty() && precedence.getOrDefault(ops.peek(), 0) >= precedence[token]!!) {
                        output.add(ops.pop())
                    }
                    ops.push(token)
                }
            }
        }
        while (ops.isNotEmpty()) output.add(ops.pop())
        return output
    }

    private fun evaluatePostfix(tokens: List<String>): Double {
        val stack = Stack<Double>()
        for (token in tokens) {
            when {
                token.matches(Regex("""\d+(\.\d+)?""")) -> stack.push(token.toDouble())
                token == "+" -> stack.push(stack.pop() + stack.pop())
                token == "-" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a - b)
                }
                token == "*" -> stack.push(stack.pop() * stack.pop())
                token == "/" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a / b)
                }
                token == "^" -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(a.pow(b))
                }
            }
        }
        return stack.pop()
    }
}