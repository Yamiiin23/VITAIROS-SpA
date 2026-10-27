package cl.vitairos.vitairosm.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cl.vitairos.vitairosm.data.local.Appointment
import cl.vitairos.vitairosm.data.local.AppointmentStatus
import cl.vitairos.vitairosm.ui.components.EmptyState
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.ScreenHeader
import cl.vitairos.vitairosm.ui.components.SoftCard
import cl.vitairos.vitairosm.ui.theme.BlueSoft
import cl.vitairos.vitairosm.ui.theme.MintSoft

@Composable
fun AgendaScreen(appointments: List<Appointment>, onNew: () -> Unit, onCancel: (Long) -> Unit) {
    var toCancel by remember { mutableStateOf<Appointment?>(null) }

    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Profesionales", "Mi agenda")
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { GradientButton("Agendar cita", onClick = onNew, icon = Icons.Outlined.Add) }
            item {
                Text(
                    "Disponibilidad y estados de citas ficticios para el prototipo.",
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (appointments.isEmpty()) {
                item { EmptyState("Sin citas", "Agenda una cita simulada para ver tu próximo paso en Inicio.") }
            }
            items(appointments, key = { it.id }) { a ->
                SoftCard(Modifier.animateItem()) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(a.professional, style = MaterialTheme.typography.titleMedium)
                            Text(a.specialty, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        StatusPill(a.status)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Event, contentDescription = null)
                        Text("${a.dateLabel} · ${a.timeLabel}", style = MaterialTheme.typography.bodyMedium)
                        Icon(if (a.modality == "Videollamada") Icons.Outlined.Videocam else Icons.Outlined.Place, contentDescription = a.modality)
                        Text(a.modality, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(a.reason, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (a.status == AppointmentStatus.SCHEDULED) {
                        TextButton(onClick = { toCancel = a }) { Text("Cancelar cita") }
                    }
                }
            }
        }
    }

    toCancel?.let { a ->
        AlertDialog(
            onDismissRequest = { toCancel = null },
            title = { Text("¿Cancelar la cita?") },
            text = { Text("Podrás agendar otra cuando quieras.") },
            confirmButton = { TextButton(onClick = { onCancel(a.id); toCancel = null }) { Text("Cancelar cita") } },
            dismissButton = { TextButton(onClick = { toCancel = null }) { Text("Volver") } }
        )
    }
}

@Composable
private fun StatusPill(status: String) {
    val target = when (status) {
        AppointmentStatus.SCHEDULED -> MintSoft
        AppointmentStatus.DONE -> BlueSoft
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val bg by animateColorAsState(target, label = "status")
    Text(
        status,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.clip(CircleShape).background(bg).padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
