package com.electromechanics.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = IndustrialYellowAccent,
    secondary = IndustrialBluePrimary,
    background = IndustrialBlack,
    surface = IndustrialCardGray,
    onPrimary = IndustrialBlack,
    onBackground = IndustrialTextWhite,
    onSurface = IndustrialTextWhite
)

@Composable
fun ElectromechanicsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}