package com.vivitasol.carcasamvvm.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = White, // Color principal para botones de elementos activos
    onPrimary = Black, // Colorr de texto encima del color primario
    background = Black, // Fondo principal de la app
    onBackground = White, // Color de texto encima del fondo
    surface = DarkGray, // Color de superficies como Cards
    onSurface = White, // Color de texto en superficies
    secondary = MediumGray, // Color secundario para elementos menos importantes
    onSecondary = White,
    tertiary = LightGray,
    onTertiary = Black
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
