package cl.vitairos.vitairosm.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.vitairos.vitairosm.ui.components.AnimatedError
import cl.vitairos.vitairosm.ui.components.Appear
import cl.vitairos.vitairosm.ui.components.BrandLogo
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.SoftCard
import cl.vitairos.vitairosm.ui.components.ValidatedTextField
import cl.vitairos.vitairosm.ui.theme.TextGradient
import cl.vitairos.vitairosm.viewmodel.AuthViewModel

@Composable
fun LoginScreen(vm: AuthViewModel, onLoggedIn: () -> Unit) {
    val s by vm.state.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        AnimatedVisibility(visible, enter = fadeIn(tween(700)) + scaleIn(tween(700), initialScale = 0.9f)) {
            BrandLogo(Modifier.widthIn(min = 240.dp).fillMaxWidth(0.85f))
        }
        Appear(150) {
            Text(
                buildAnnotatedString {
                    append("Que pedir ayuda no signifique ")
                    withStyle(SpanStyle(brush = TextGradient)) { append("empezar de cero.") }
                },
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Appear(250) {
            SoftCard {
                Text("Ingresa con tu perfil de prueba", style = MaterialTheme.typography.titleMedium)
                ValidatedTextField(
                    value = s.email, onValueChange = vm::onEmail, label = "Correo electrónico",
                    error = s.emailError, leadingIcon = Icons.Outlined.Email, keyboardType = KeyboardType.Email
                )
                ValidatedTextField(
                    value = s.password, onValueChange = vm::onPassword, label = "Contraseña",
                    error = s.passwordError, leadingIcon = Icons.Outlined.Lock, keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done, isPassword = true, passwordVisible = s.showPassword,
                    onTogglePassword = vm::togglePassword
                )
                AnimatedError(s.authError)
                GradientButton("Ingresar", onClick = { vm.submit(onLoggedIn) }, loading = s.loading)
                TextButton(onClick = vm::useDemoProfile, modifier = Modifier.fillMaxWidth()) {
                    Text("Usar perfil ficticio de prueba")
                }
            }
        }
        Text(
            "Prototipo académico: solo datos ficticios. VITAIROS-M no entrega diagnósticos ni reemplaza la atención profesional.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
