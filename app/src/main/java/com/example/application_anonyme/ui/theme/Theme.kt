package com.example.application_anonyme.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 🌙 Palette sombre (mauve profond + rose néon)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB388FF),      // Mauve clair
    onPrimary = Color(0xFF1E1E1E),
    secondary = Color(0xFFF48FB1),    // Rose doux
    onSecondary = Color(0xFF000000),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White
)

// 🌞 Palette claire (rose poudré + mauve pastel)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFBA68C8),      // Mauve clair
    onPrimary = Color.White,
    secondary = Color(0xFFF06292),    // Rose doux
    onSecondary = Color.White,
    background = Color(0xFFFDF3F7),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF333333),
    onSurface = Color(0xFF333333)
)

@Composable
fun Application_anonymeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // 🌸 on désactive le dynamic pour garder ton rose/mauve fixe
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
