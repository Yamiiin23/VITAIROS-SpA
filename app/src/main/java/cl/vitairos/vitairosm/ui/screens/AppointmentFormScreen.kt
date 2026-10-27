package cl.vitairos.vitairosm.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.vitairos.vitairosm.model.Catalog
import cl.vitairos.vitairosm.util.Validators
import cl.vitairos.vitairosm.ui.components.AnimatedError
import cl.vitairos.vitairosm.ui.components.ChoiceChips
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.ScreenHeader
import cl.vitairos.vitairosm.ui.components.SectionLabel
import cl.vitairos.vitairosm.ui.components.ValidatedTextField
import cl.vitairos.vitairosm.util.Notifier
import cl.vitairos.vitairosm.viewmodel.AppointmentViewModel

@Composable
fun AppointmentFormScreen(vm: AppointmentViewModel, onBack: () -> Unit, onSaved: () -> Unit) {
    val form by vm.form.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val days = remember { Catalog.nextDays() }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        vm.onReminder(granted)
    }

    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Agenda", "Agendar cita simulada", onBack = { vm.resetForm(); onBack() })
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).imePadding().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionLabel("Profesional")
            Catalog.professionals.forEach { p ->
                val selected = form.professional == p
                val border by animateColorAsState(
                    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, label = "border"
                )
                Card(
                    Modifier.fillMaxWidth().selectable(selected = selected, role = Role.RadioButton, onClick = { vm.onProfessional(p) }),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(if (selected) 2.dp else 1.dp, border),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selected, onClick = null)
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(p.name, style = MaterialTheme.typography.titleMedium)
                            Text(p.specialty, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            AnimatedError(form.professionalError)

            SectionLabel("Fecha")
            ChoiceChips(days, form.date, vm::onDate, form.dateError)
            SectionLabel("Horario")
            ChoiceChips(Catalog.times, form.time, vm::onTime, form.timeError)
            SectionLabel("Modalidad")
            ChoiceChips(Catalog.modalities, form.modality, vm::onModality, null)

            ValidatedTextField(
                value = form.reason, onValueChange = vm::onReason, label = "Motivo de la cita",
                error = form.reasonError, leadingIcon = Icons.Outlined.EditNote,
                hint = "Describe el motivo con datos de prueba.", imeAction = ImeAction.Done,
                singleLine = false, minLines = 3, maxChars = Validators.REASON_MAX
            )

            Row(
                Modifier.fillMaxWidth().toggleable(value = form.reminder, role = Role.Switch, onValueChange = { on ->
                    if (on && Build.VERSION.SDK_INT >= 33 && !Notifier.hasPermission(context)) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else vm.onReminder(on)
                }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Recordatorio simulado", style = MaterialTheme.typography.titleMedium)
                    Text("Recibirás una notificación local de prueba.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = form.reminder, onCheckedChange = null)
            }

            Row(
                Modifier.fillMaxWidth().toggleable(value = form.consent, role = Role.Checkbox, onValueChange = vm::onConsent),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(checked = form.consent, onCheckedChange = null)
                Text(
                    "Entiendo que esta cita es simulada, que VITAIROS-M no es un servicio de emergencia y que no reemplaza la atención profesional.",
                    style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
            }
            AnimatedError(form.consentError)

            GradientButton("Confirmar cita", icon = Icons.Outlined.Check, onClick = {
                vm.save { appointment, reminder ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (reminder) {
                        Notifier.show(
                            context, "Recordatorio de cita (simulado)",
                            "${appointment.dateLabel} a las ${appointment.timeLabel} con ${appointment.professional}"
                        )
                    }
                    onSaved()
                }
            })
        }
    }
}
