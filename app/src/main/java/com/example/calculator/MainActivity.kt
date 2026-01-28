package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import com.example.calculator.data.local.LocalStorage
import com.example.calculator.data.local.PassKeyManager
import com.example.calculator.data.remote.HistoryRepository
import com.example.calculator.data.remote.ThemeData
import com.example.calculator.data.remote.ThemeRepository
import com.example.calculator.data.repository.CombinedHistoryRepository
import com.example.calculator.ui.screens.CalculatorScreen
import com.example.calculator.ui.screens.PassKeyScreen
import com.example.calculator.ui.theme.CalculatorTheme
import com.example.calculator.utils.BiometricUtils
import com.example.calculator.utils.PlatformUtils
import com.example.calculator.utils.ShakeDetector
import com.example.calculator.viewmodel.CalculatorViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

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

        val passKeyManager = PassKeyManager(this)
        val biometricUtils = BiometricUtils()

        setContent {
            val scope = rememberCoroutineScope()

            var isAuthorized by remember { mutableStateOf(false) }
            var isSettingNew by remember { mutableStateOf(false) }

            var lightTheme by remember { mutableStateOf<ThemeData?>(null) }
            var darkTheme by remember { mutableStateOf<ThemeData?>(null) }
            val systemDarkTheme = isSystemInDarkTheme()
            var useDarkTheme by remember { mutableStateOf(systemDarkTheme) }

            LaunchedEffect(Unit) {
                val (light, dark) = themeRepository.loadAllThemes(userId)
                lightTheme = light
                darkTheme = dark

                if (!passKeyManager.isPassKeySet()) {
                    passKeyManager.setPassKey("1234")
                    isAuthorized = true
                }
            }

            if (!isAuthorized) {
                PassKeyScreen(
                    passKeyManager = passKeyManager,
                    onSuccess = {
                        isAuthorized = true
                        isSettingNew = false },
                    onReset = { isSettingNew = true },
                    biometricUtils = biometricUtils
                )
            } else {
                val currentTheme =
                    if (useDarkTheme) darkTheme ?: ThemeData() else lightTheme ?: ThemeData()
                CalculatorTheme(
                    themeData = currentTheme,
                    darkTheme = useDarkTheme
                ) {
                    CalculatorScreen(
                        viewModel = viewModel,
                        platformUtils = platformUtils,
                        onThemeChange = {
                            useDarkTheme = !useDarkTheme
                            scope.launch {
                                themeRepository.saveTheme(
                                    userId,
                                    "light",
                                    lightTheme ?: ThemeData()
                                )
                                themeRepository.saveTheme(
                                    userId,
                                    "dark",
                                    darkTheme ?: ThemeData()
                                )
                            }
                        }
                    )
                }
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
