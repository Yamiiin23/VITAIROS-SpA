package cl.vitairos.vitairosm.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Scheme = lightColorScheme(
    primary = CyanText,
    onPrimary = BaseWhite,
    primaryContainer = BlueSoft,
    onPrimaryContainer = Navy,
    secondary = GreenText,
    onSecondary = BaseWhite,
    secondaryContainer = MintSoft,
    onSecondaryContainer = Navy,
    tertiary = BlueText,
    onTertiary = BaseWhite,
    background = BaseWhite,
    onBackground = Navy,
    surface = BaseWhite,
    onSurface = Navy,
    surfaceVariant = SoftSection,
    onSurfaceVariant = SlateBlue,
    outline = SlateBlue,
    outlineVariant = SoftBorder,
    surfaceContainer = SoftSection
)

private val BrandFont = FontFamily.SansSerif
private val base = Typography()

private val BrandTypography = Typography(
    headlineMedium = base.headlineMedium.copy(fontFamily = BrandFont, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp),
    titleLarge = base.titleLarge.copy(fontFamily = BrandFont, fontWeight = FontWeight.SemiBold),
    titleMedium = base.titleMedium.copy(fontFamily = BrandFont, fontWeight = FontWeight.SemiBold),
    bodyLarge = base.bodyLarge.copy(fontFamily = BrandFont),
    bodyMedium = base.bodyMedium.copy(fontFamily = BrandFont),
    labelLarge = base.labelLarge.copy(fontFamily = BrandFont, fontWeight = FontWeight.Bold),
    labelSmall = base.labelSmall.copy(fontFamily = BrandFont, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
)

private val BrandShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun VitairosTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, typography = BrandTypography, shapes = BrandShapes, content = content)
}
