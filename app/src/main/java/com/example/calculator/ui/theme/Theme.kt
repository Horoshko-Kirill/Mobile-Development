package com.example.calculator.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.calculator.data.remote.ThemeData

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun CalculatorTheme(
    themeData: ThemeData? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        else -> {
            if (themeData != null) {
                lightColorScheme(
                    primary = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(themeData.primaryColor)),
                    secondary = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(themeData.secondaryColor)),
                    tertiary = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(themeData.tertiaryColor))
                )
            } else {
                if (darkTheme) DarkColorScheme else LightColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
