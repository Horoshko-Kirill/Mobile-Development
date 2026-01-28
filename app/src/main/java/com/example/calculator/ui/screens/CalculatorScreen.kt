package com.example.calculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.ui.components.CalcButton
import com.example.calculator.ui.components.Display
import com.example.calculator.viewmodel.CalculatorViewModel
import com.example.calculator.ui.theme.*
import com.example.calculator.utils.PlatformUtils
import androidx.compose.runtime.*
import com.example.calculator.data.remote.ThemeData
import androidx.compose.material3.Button


@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    platformUtils: PlatformUtils,
    onThemeChange: () -> Unit
) {



    val state by viewModel.state.collectAsState()
    val history by viewModel.history.collectAsState()

    var copyResult by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(copyResult) {
        copyResult?.let {
            platformUtils.copyToClipboard("Result", it)
            copyResult = null // сбрасываем
        }
    }

    val buttons = listOf(
        listOf("sin","cos","tan","ln","log"),
        listOf("√","!","^","C","%"),
        listOf("7","8","9","/"),
        listOf("4","5","6","*"),
        listOf("1","2","3","-"),
        listOf("0",".","=","+")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            reverseLayout = true,
            verticalArrangement = Arrangement.Bottom
        ) {
            items(history.takeLast(1000)) { entry ->
                Text(
                    text = entry,
                    color = TextColor.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
            }
        }

        Button(
            onClick = onThemeChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Сменить тему")
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Display(
            text = state.display,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { label ->
                    CalcButton(
                        label = label,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        shape = true
                    ) {

                        platformUtils.vibrate()
                        platformUtils.playClick()

                        try {
                            when(label) {
                                "C" -> {
                                    viewModel.onClear()
                                    platformUtils.showToast("Очистка дисплея")
                                }
                                "=" -> {
                                    viewModel.onEquals()
                                    copyResult = viewModel.state.value.display
                                }
                                in listOf("+","-","*","/","^","%") -> viewModel.onOperatorClick(label)
                                in listOf("√","!","sin","cos","tan","ln","log","exp") -> viewModel.onUnaryOperation(label)
                                else -> viewModel.onNumberClick(label)
                            }
                        } catch (e: Exception) {
                            platformUtils.showToast("Ошибка: ${e.message}")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
