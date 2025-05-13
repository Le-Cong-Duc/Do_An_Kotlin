package com.example.chatter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00BCD4),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFE6FFE6),
    onSurface = Color(0xFFF3F9FC)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE2ECF1),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFFF3C3D1),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFFC3E6FF),
    onBackground = Color(0xFFD5D2D2),
    onSurface = Color(0xFFB1DCEF)
)

@Composable
fun HRAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
