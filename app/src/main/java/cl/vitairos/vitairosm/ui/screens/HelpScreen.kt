package cl.vitairos.vitairosm.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.vitairos.vitairosm.ui.components.Appear
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.ScreenHeader
import cl.vitairos.vitairosm.ui.components.SoftCard
import cl.vitairos.vitairosm.ui.theme.MintSoft

private data class Contact(val name: String, val display: String, val dial: String)

private val contacts = listOf(
    Contact("Línea de prevención del suicidio (Minsal)", "*4141", "*4141"),
    Contact("Salud Responde (24 h)", "600 360 7777", "6003607777"),
    Contact("Emergencias de salud (SAMU)", "131", "131")
)

/** Recurso nativo: abre el marcador del teléfono (no requiere permiso de llamadas). */
private fun dial(context: Context, number: String, onFail: () -> Unit) {
    try {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number))))
    } catch (e: ActivityNotFoundException) {
        onFail()
    }
}

@Composable
fun HelpScreen(onLogout: () -> Unit, showMessage: (String) -> Unit) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Seguridad", "Ayuda y límites")
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Appear(0) {
                SoftCard(container = MintSoft) {
                    Text("Si estás en riesgo o necesitas ayuda ahora", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Esta app es un prototipo de orientación y no atiende emergencias. Contacta de inmediato a un servicio de ayuda.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    contacts.forEach { c ->
                        GradientButton(
                            "${c.name}: ${c.display}", icon = Icons.Outlined.Phone,
                            onClick = { dial(context, c.dial) { showMessage("No se encontró una app de teléfono.") } }
                        )
                    }
                }
            }
            Appear(100) {
                SoftCard {
                    Text("Qué es y qué no es VITAIROS-M", style = MaterialTheme.typography.titleMedium)
                    listOf(
                        "Apoya el registro y la continuidad entre sesiones.",
                        "Ayuda a organizar tus próximos pasos con profesionales.",
                        "No entrega diagnósticos ni indicaciones clínicas.",
                        "No reemplaza la atención de un profesional de salud mental."
                    ).forEach {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            Appear(200) {
                SoftCard {
                    Text("Privacidad", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Todos los datos de este prototipo son ficticios y se guardan solo en este teléfono.",
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                Text("  Cerrar sesión")
            }
        }
    }
}
