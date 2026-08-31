package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = BrandTealPrimary,
  onPrimary = TextInverse,
  primaryContainer = PastelSkyBg,
  onPrimaryContainer = PastelSkyText,
  secondary = BrandIndigo,
  onSecondary = TextInverse,
  secondaryContainer = PastelIndigoBg,
  onSecondaryContainer = PastelIndigoText,
  tertiary = ActionTeal,
  onTertiary = TextInverse,
  tertiaryContainer = PastelEmeraldBg,
  onTertiaryContainer = PastelEmeraldText,
  background = CanvasBackground,
  onBackground = TextPrimary,
  surface = SurfaceCard,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceCardSubtle,
  onSurfaceVariant = TextSecondary,
  outline = DividerLight,
  outlineVariant = BorderSubtle,
  error = PastelRoseText,
  errorContainer = PastelRoseBg,
  onError = TextInverse,
  onErrorContainer = PastelRoseText
)

private val DarkColorScheme = darkColorScheme(
  primary = ActionTealLight,
  onPrimary = TextPrimary,
  primaryContainer = BrandTealDark,
  onPrimaryContainer = CanvasBackground,
  secondary = PastelIndigoBorder,
  onSecondary = TextPrimary,
  background = Color(0xFF0F172A),
  surface = Color(0xFF1E293B),
  onBackground = Color(0xFFF8FAFC),
  onSurface = Color(0xFFF8FAFC),
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF475569)
)

@Composable
fun RepairReachTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  // We prioritize high-contrast crisp light outdoor theme for field technicians
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
