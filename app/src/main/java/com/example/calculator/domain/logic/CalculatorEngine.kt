package com.example.calculator.domain.logic

import kotlin.math.*

class CalculatorEngine {

    fun calculate(a: Double?, b: Double?, op: String): Double {
        return try {
            when (op) {
                "+" -> (a ?: 0.0) + (b ?: 0.0)
                "-" -> (a ?: 0.0) - (b ?: 0.0)
                "*" -> (a ?: 0.0) * (b ?: 0.0)
                "/" -> if (b == 0.0) Double.NaN else (a ?: 0.0) / (b ?: 0.0)
                "^" -> Math.pow((a ?: 0.0), (b ?: 0.0))
                "√" -> Math.sqrt((a ?: 0.0))
                "!" -> factorial(a ?: 0.0)
                "sin" -> Math.sin(Math.toRadians(a ?: 0.0))
                "cos" -> Math.cos(Math.toRadians(a ?: 0.0))
                "tan" -> Math.tan(Math.toRadians(a ?: 0.0))
                "ln" -> Math.log(a ?: 1.0)
                "log" -> Math.log10(a ?: 1.0)
                "exp" -> Math.exp(a ?: 0.0)
                "±" -> -(a ?: 0.0)
                "%" -> (a ?: 0.0) / 100.0
                else -> b ?: 0.0
            }
        } catch (e: Exception) {
            Double.NaN
        }
    }

    private fun factorial(x: Double): Double {
        return if (x < 0) Double.NaN else (1..x.toInt()).fold(1.0) { acc, i -> acc * i }
    }
}
