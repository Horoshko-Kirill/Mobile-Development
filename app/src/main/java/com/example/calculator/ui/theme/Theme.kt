package com.example.calculator.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.calculator.data.remote.ThemeData
import androidx.compose.ui.graphics.Color

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
    val colorScheme = if (themeData != null) {
        if (darkTheme) {
            darkColorScheme(
                primary = Color(android.graphics.Color.parseColor(themeData.primaryColor)),
                secondary = Color(android.graphics.Color.parseColor(themeData.secondaryColor)),
                tertiary = Color(android.graphics.Color.parseColor(themeData.tertiaryColor))
            )
        } else {
            lightColorScheme(
                primary = Color(android.graphics.Color.parseColor(themeData.primaryColor)),
                secondary = Color(android.graphics.Color.parseColor(themeData.secondaryColor)),
                tertiary = Color(android.graphics.Color.parseColor(themeData.tertiaryColor))
            )
        }
    } else {
        if (darkTheme) DarkColorScheme else LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
