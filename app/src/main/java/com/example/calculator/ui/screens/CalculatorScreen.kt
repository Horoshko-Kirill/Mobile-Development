package com.example.calculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.ui.components.CalcButton
import com.example.calculator.ui.components.Display
import com.example.calculator.viewmodel.CalculatorViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color


@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {

    val state by viewModel.state.collectAsState()
    val history by viewModel.history.collectAsState(emptyList())

    val buttons = listOf(
        "7","8","9","/","√",
        "4","5","6","*","^",
        "1","2","3","-","!",
        "0",".","=","+","C",
        "sin","cos","tan","ln","log"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Display(text = state.display)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(buttons) { label ->
                CalcButton(label = label) {
                    when(label) {
                        "C" -> viewModel.onClear()
                        "=" -> viewModel.onEquals()
                        in listOf("+","-","*","/","^","%") -> viewModel.onOperatorClick(label)
                        in listOf("√","!","±","sin","cos","tan","ln","log","exp") -> viewModel.onUnaryOperation(label)
                        else -> viewModel.onNumberClick(label)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("History:", color = Color.Gray)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(history) { entry ->
                Text(entry, color = Color.Gray)
            }
        }
    }
}
