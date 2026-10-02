package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4C8DFF), // Vibrant Blue for Dark Mode buttons & highlights
    onPrimary = Color.White,

    secondary = Color(0xFF90CAF9),
    onSecondary = Color.Black,

    tertiary = Color(0xFF80DEEA),
    onTertiary = Color.Black,

    background = Color(0xFF0F172A), // Deep Slate Dark Background
    onBackground = Color(0xFFF8FAFC), // High-contrast White for background text

    surface = Color(0xFF1E293B), // Elevated Dark Card Surface
    onSurface = Color(0xFFF8FAFC), // High-contrast White for text inside surface cards

    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1), // Light Gray for labels & muted text

    outline = Color(0xFF64748B) // Border color for input fields
)

private val LightColorScheme = lightColorScheme(
    primary = Ink, // Dark Navy Blue
    onPrimary = AppWhite,

    secondary = InkMuted,
    onSecondary = AppWhite,

    tertiary = Accent,
    onTertiary = AppWhite,

    background = Canvas, // Pure White
    onBackground = Charcoal,

    surface = AppWhite,
    onSurface = Charcoal,

    surfaceVariant = SurfaceTint,
    onSurfaceVariant = Muted,

    outline = Border
)

@Composable
fun DisasterManagmentAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
