package com.example.calculator.domain.model

data class CalculatorState(
    val display: String = "0",
    val firstNumber: Double? = null,
    val operator: String? = null,
    val isNewInput: Boolean = true
)