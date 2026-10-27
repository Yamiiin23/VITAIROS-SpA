package cl.vitairos.vitairosm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import cl.vitairos.vitairosm.data.local.MoodEntry
import cl.vitairos.vitairosm.model.Catalog
import cl.vitairos.vitairosm.ui.components.EmptyState
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.MoodIcons
import cl.vitairos.vitairosm.ui.components.ScreenHeader
import cl.vitairos.vitairosm.ui.components.SoftCard
import cl.vitairos.vitairosm.ui.theme.BlueSoft
import cl.vitairos.vitairosm.util.Formatters

@Composable
fun HistoryScreen(moods: List<MoodEntry>, onNew: () -> Unit, onDelete: (MoodEntry) -> Unit) {
    var toDelete by remember { mutableStateOf<MoodEntry?>(null) }

    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Seguimiento", "Mis registros")
        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            item { GradientButton("Nuevo registro", onClick = onNew, icon = Icons.Outlined.Add) }
            if (moods.isEmpty()) {
                item { EmptyState("Aún no hay registros", "Cuando registres cómo te sientes, tu historial aparecerá aquí.") }
            }
            items(moods, key = { it.id }) { entry ->
                SoftCard(Modifier.animateItem()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                        Box(Modifier.size(44.dp).clip(CircleShape).background(BlueSoft), contentAlignment = Alignment.Center) {
                            Icon(MoodIcons[entry.mood - 1], contentDescription = Catalog.moodLabels[entry.mood - 1], tint = MaterialTheme.colorScheme.primary)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(entry.domain, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${Catalog.moodLabels[entry.mood - 1]} · ${Formatters.dateTime(entry.createdAt)}",
                                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (entry.note.isNotBlank()) Text(entry.note, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { toDelete = entry }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Eliminar registro")
                        }
                    }
                }
            }
        }
    }

    toDelete?.let { entry ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("¿Eliminar registro?") },
            text = { Text("Se borrará de este teléfono. Esta acción no se puede deshacer.") },
            confirmButton = { TextButton(onClick = { onDelete(entry); toDelete = null }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { toDelete = null }) { Text("Cancelar") } }
        )
    }
}
