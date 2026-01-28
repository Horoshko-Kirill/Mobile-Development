package com.example.calculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.data.local.LocalStorage
import com.example.calculator.domain.logic.CalculatorEngine
import com.example.calculator.domain.model.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.calculator.data.remote.HistoryRepository
import com.example.calculator.data.repository.CombinedHistoryRepository

class CalculatorViewModel(
    private val combinedHistory: CombinedHistoryRepository? = null,
    private val userId: String = "default_user"
) : ViewModel() {

    private val engine = CalculatorEngine()

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history

    init {
        viewModelScope.launch {
            combinedHistory?.let { repo ->
                _history.value = repo.getHistory(userId).reversed() // последние сверху
            }
        }
    }
    fun onNumberClick(number: String) {
        val current = _state.value

        if (current.display.length >= 12 && !current.isNewInput) return

        val newDisplay = if (current.isNewInput) number else current.display + number

        if (number == "." && current.display.contains(".")) return

        _state.value = current.copy(display = newDisplay, isNewInput = false)
    }


    fun onOperatorClick(op: String) {
        val current = _state.value
        _state.value = current.copy(
            firstNumber = current.display.toDoubleOrNull(),
            operator = op,
            isNewInput = true
        )
    }

    fun onUnaryOperation(op: String) {
        val current = _state.value
        val number = current.display.toDoubleOrNull() ?: 0.0
        val result = engine.calculate(number, null, op)
        _state.value = current.copy(display = formatResult(result), isNewInput = true)
        saveToHistory("${op} $number = ${formatResult(result)}")
    }

    fun onEquals() {
        val current = _state.value
        val first = current.firstNumber ?: return
        val second = current.display.toDoubleOrNull() ?: return
        val op = current.operator ?: return
        val result = engine.calculate(first, second, op)
        val formatted = formatResult(result)
        _state.value = current.copy(
            display = formatted,
            firstNumber = null,
            operator = null,
            isNewInput = true
        )
        saveToHistory("$first $op $second = $formatted")
    }


    fun onClear() {
        _state.value = CalculatorState()
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
    }

    private fun saveToHistory(entry: String) {
        _history.value = listOf(entry) + _history.value
        combinedHistory?.let { repo ->
            viewModelScope.launch {
                repo.saveEntry(userId, entry)
            }
        }
    }


}
