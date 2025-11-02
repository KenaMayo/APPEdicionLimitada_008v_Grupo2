package com.vivitasol.carcasamvvm.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vivitasol.carcasamvvm.R

val Michroma = FontFamily(
    Font(R.font.michroma_regular, FontWeight.Normal)
)

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal)
)

// Set of Material typography styles to start with
val AppTypography = Typography(
    // Usado para títulos grandes, como en los headers
    displayLarge = TextStyle(
        fontFamily = Michroma,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.5.sp
    ),
    // Títulos de seccion
    headlineLarge = TextStyle(
        fontFamily = Michroma,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp
    ),
    // Título principal de los textos
    titleLarge = TextStyle(
        fontFamily = Michroma,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    // Cuerpo de texto principal
    bodyLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Texto para botones y labels
    labelLarge = TextStyle(
        fontFamily = Michroma,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)
