package com.riteshapps.zomatoclone.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ZomatoLightColorScheme = lightColorScheme(
    primary = ZomatoRed,
    onPrimary = TextOnPrimary,
    primaryContainer = ZomatoRedLight,
    onPrimaryContainer = ZomatoRedDark,
    secondary = RatingGreen,
    onSecondary = TextOnPrimary,
    secondaryContainer = OfferBackground,
    onSecondaryContainer = OfferText,
    tertiary = DiscountBlue,
    onTertiary = TextOnPrimary,
    error = NonVegBrown,
    onError = TextOnPrimary,
    background = BackgroundWhite,
    onBackground = TextPrimary,
    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundGrey,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    outlineVariant = DividerColor
)

private val ZomatoDarkColorScheme = darkColorScheme(
    primary = ZomatoRed,
    onPrimary = TextOnPrimary,
    primaryContainer = ZomatoRedDark,
    onPrimaryContainer = ZomatoRedLight,
    secondary = RatingGreen,
    onSecondary = TextOnPrimary,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = OfferText,
    tertiary = DiscountBlue,
    onTertiary = TextOnPrimary,
    error = NonVegBrown,
    onError = TextOnPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorder
)

@Composable
fun ZomatoCloneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ZomatoDarkColorScheme else ZomatoLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ZomatoTypography,
        content = content
    )
}