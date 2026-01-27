package com.example.calculator.domain.logic

import kotlin.math.*

class CalculatorEngine {

    fun calculate(a: Double?, b: Double?, op: String): Double {
        return try {
            when (op) {
                "+" -> safe(a) + safe(b)
                "-" -> safe(a) - safe(b)
                "*" -> safe(a) * safe(b)
                "/" -> if (safe(b) == 0.0) Double.NaN else safe(a) / safe(b)
                "^" -> {
                    val base = safe(a)
                    val exp = safe(b)
                    if (base == 0.0 && exp <= 0.0 || base > 2e9) Double.NaN
                    else {
                        val result = try {
                            base.pow(exp)
                        } catch (e: Exception) {
                            Double.NaN
                        }
                        if (result.isInfinite() || result.isNaN()) Double.NaN else result
                    }
                }

                "√" -> {
                    val value = safe(a)
                    if (value < 0) Double.NaN else sqrt(value)
                }
                "!" -> factorial(safe(a))
                "sin" -> sin(Math.toRadians(safe(a)))
                "cos" -> cos(Math.toRadians(safe(a)))
                "tan" -> tan(Math.toRadians(safe(a)))
                "ln" -> {
                    val value = safe(a)
                    if (value <= 0) Double.NaN else ln(value)
                }
                "log" -> {
                    val value = safe(a)
                    if (value <= 0) Double.NaN else log10(value)
                }
                "exp" -> exp(safe(a))
                "±" -> -safe(a)
                "%" -> safe(a) / 100.0
                else -> safe(b)
            }
        } catch (e: Exception) {
            Double.NaN
        }
    }

    private fun safe(x: Double?): Double {
        return if (x == null || x.isNaN() || x.isInfinite()) 0.0 else x
    }


    private fun factorial(x: Double): Double {
        val n = x.toInt()
        return if (x < 0 || n != x.toInt() || x > 170 || x.isNaN() || x.isInfinite()) {
            Double.NaN
        } else {
            (1..n).fold(1.0) { acc, i -> acc * i }
        }
    }
}
