package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.calculator.data.local.LocalStorage
import com.example.calculator.ui.screens.CalculatorScreen
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.utils.PlatformUtils
import com.example.calculator.utils.ShakeDetector
import com.example.calculator.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {

    private lateinit var storage: LocalStorage
    private lateinit var platformUtils: PlatformUtils
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = LocalStorage(this)
        platformUtils = PlatformUtils(this)

        // Загрузи звук клика, положи файл res/raw/click_sound.mp3
        platformUtils.loadClickSound(R.raw.click_sound)

        viewModel = CalculatorViewModel(storage)

        setContent {
            CalculatorTheme {
                CalculatorScreen(viewModel = viewModel, platformUtils = platformUtils)
            }
        }

        shakeDetector = ShakeDetector {
            viewModel.onClear()
            platformUtils.showToast("Очистка дисплея")
            platformUtils.vibrate(100)
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start(this)
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }
}
