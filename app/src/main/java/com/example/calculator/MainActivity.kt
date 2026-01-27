package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.calculator.ui.screens.CalculatorScreen
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.data.local.LocalStorage

class MainActivity : ComponentActivity() {
    private lateinit var storage: LocalStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = LocalStorage(this)

        setContent {
            CalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}
