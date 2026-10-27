package cl.vitairos.vitairosm.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Tokens del Manual de Identidad Visual VITAIROS-M v3.0
val Navy = Color(0xFF000E2E)
val SlateBlue = Color(0xFF536477)
val BrandGreen = Color(0xFF00C98E)
val BrandCyan = Color(0xFF00B9D3)
val BrandBlue = Color(0xFF00A5F4)
val CyanText = Color(0xFF007F9E)
val BaseWhite = Color(0xFFFEFEFE)
val SoftSection = Color(0xFFF2F8FB)
val MintSoft = Color(0xFFDDF6F0)
val BlueSoft = Color(0xFFE1F3FE)
val GreenText = Color(0xFF008E70)
val BlueText = Color(0xFF006FB8)
val SoftBorder = Color(0xFFD5E3EB)

/** Gradiente principal: solo para acentos, progreso y llamadas a la acción. */
val BrandGradient = Brush.horizontalGradient(0f to BrandGreen, 0.54f to BrandCyan, 1f to BrandBlue)

/** Gradiente de texto: solo para palabras clave sobre fondos claros. */
val TextGradient = Brush.horizontalGradient(0f to GreenText, 0.54f to CyanText, 1f to BlueText)
