package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.calculator.data.local.LocalStorage
import com.example.calculator.data.remote.HistoryRepository
import com.example.calculator.data.remote.ThemeData
import com.example.calculator.data.remote.ThemeRepository
import com.example.calculator.data.repository.CombinedHistoryRepository
import com.example.calculator.ui.screens.CalculatorScreen
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.utils.PlatformUtils
import com.example.calculator.utils.ShakeDetector
import com.example.calculator.viewmodel.CalculatorViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {

    private lateinit var storage: LocalStorage
    private lateinit var platformUtils: PlatformUtils
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var viewModel: CalculatorViewModel

    private val themeRepository = ThemeRepository()
    private val userId = "default_user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        storage = LocalStorage(this)
        platformUtils = PlatformUtils(this)
        platformUtils.loadClickSound(R.raw.click_sound)


        val remoteHistory = HistoryRepository()
        val combinedHistory = CombinedHistoryRepository(storage, remoteHistory)

        viewModel = CalculatorViewModel(combinedHistory)

        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var useDarkTheme by remember { mutableStateOf(systemDarkTheme) }

            var lightTheme by remember { mutableStateOf<ThemeData?>(null) }
            var darkTheme by remember { mutableStateOf<ThemeData?>(null) }


            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                val (light, dark) = themeRepository.loadAllThemes(userId)
                lightTheme = light
                darkTheme = dark
            }

            CalculatorTheme(
                themeData = if (useDarkTheme) darkTheme else lightTheme,
                darkTheme = useDarkTheme
            ) {
                CalculatorScreen(
                    viewModel = viewModel,
                    platformUtils = platformUtils,
                    onThemeChange = {
                        useDarkTheme = !useDarkTheme
                        scope.launch {
                            themeRepository.saveTheme(userId, "light", lightTheme ?: ThemeData())
                            themeRepository.saveTheme(userId, "dark", darkTheme ?: ThemeData())
                        }
                    }
                )
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
