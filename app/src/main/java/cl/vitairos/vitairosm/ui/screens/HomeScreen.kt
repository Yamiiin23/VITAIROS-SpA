package cl.vitairos.vitairosm.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cl.vitairos.vitairosm.data.local.Appointment
import cl.vitairos.vitairosm.data.local.AppointmentStatus
import cl.vitairos.vitairosm.data.local.MoodEntry
import cl.vitairos.vitairosm.model.Catalog
import cl.vitairos.vitairosm.ui.components.Appear
import cl.vitairos.vitairosm.ui.components.BrandLogo
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.SafetyBanner
import cl.vitairos.vitairosm.ui.components.SoftCard
import cl.vitairos.vitairosm.ui.theme.BlueSoft
import cl.vitairos.vitairosm.ui.theme.TextGradient

@Composable
fun HomeScreen(
    userName: String,
    moods: List<MoodEntry>,
    appointments: List<Appointment>,
    onNewMood: () -> Unit,
    onAgenda: () -> Unit,
    onNewAppointment: () -> Unit,
    onHelp: () -> Unit
) {
    val next = appointments.firstOrNull { it.status == AppointmentStatus.SCHEDULED }
    val weekAgo = System.currentTimeMillis() - 7 * 86_400_000L
    val weekCount = moods.count { it.createdAt >= weekAgo }
    val progress by animateFloatAsState((weekCount / 7f).coerceIn(0f, 1f), tween(900), label = "week")

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Appear(0) { BrandLogo(Modifier.width(220.dp)) }
        Appear(80) {
            Column {
                Text("TU ACOMPAÑAMIENTO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text("Hola, ${userName.substringBefore(" ")}", style = MaterialTheme.typography.headlineMedium)
                Text(
                    buildAnnotatedString { withStyle(SpanStyle(brush = TextGradient)) { append("La vida no ocurre solo en la sesión.") } },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Appear(140) { SafetyBanner(onHelp) }
        Appear(200) {
            SoftCard(container = BlueSoft) {
                Text("TU PRÓXIMO PASO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                if (next != null) {
                    Text("Cita con ${next.professional}", style = MaterialTheme.typography.titleMedium)
                    Text("${next.dateLabel} · ${next.timeLabel} · ${next.modality}", style = MaterialTheme.typography.bodyMedium)
                    TextButton(onClick = onAgenda) { Text("Ver agenda") }
                } else {
                    Text("Registra cómo te sientes hoy", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Un registro breve ayuda a mantener el contexto entre sesiones.",
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Appear(260) {
            SoftCard {
                Text("CONTINUIDAD ESTA SEMANA", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = BlueSoft
                )
                Text("$weekCount de 7 días con registro", style = MaterialTheme.typography.bodyMedium)
                moods.firstOrNull()?.let {
                    Text(
                        "Último registro: ${Catalog.moodLabels[it.mood - 1]} · ${it.domain}",
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Appear(320) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GradientButton("Registrar cómo me siento", onClick = onNewMood, icon = Icons.Outlined.EditNote)
                OutlinedButton(onClick = onNewAppointment, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    Icon(Icons.Outlined.Event, contentDescription = null)
                    Text("  Agendar cita")
                }
            }
        }
    }
}
