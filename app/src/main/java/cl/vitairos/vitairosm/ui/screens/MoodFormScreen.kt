package cl.vitairos.vitairosm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.vitairos.vitairosm.model.Catalog
import cl.vitairos.vitairosm.util.Validators
import cl.vitairos.vitairosm.ui.components.ChoiceChips
import cl.vitairos.vitairosm.ui.components.GradientButton
import cl.vitairos.vitairosm.ui.components.MoodSelector
import cl.vitairos.vitairosm.ui.components.ScreenHeader
import cl.vitairos.vitairosm.ui.components.SectionLabel
import cl.vitairos.vitairosm.ui.components.ValidatedTextField
import cl.vitairos.vitairosm.viewmodel.MoodViewModel

@Composable
fun MoodFormScreen(vm: MoodViewModel, onBack: () -> Unit, onSaved: () -> Unit) {
    val form by vm.form.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Registro emocional", "¿Cómo te sientes hoy?", onBack = { vm.resetForm(); onBack() })
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).imePadding().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MoodSelector(current = form.mood, onSelect = vm::onMood, error = form.moodError)
            SectionLabel("Ámbito de seguimiento")
            ChoiceChips(Catalog.domains, form.domain, vm::onDomain, form.domainError)
            ValidatedTextField(
                value = form.note, onValueChange = vm::onNote, label = "Nota (opcional)",
                error = form.noteError, leadingIcon = Icons.Outlined.EditNote,
                hint = "Usa solo datos de prueba, sin información personal real.",
                imeAction = ImeAction.Done, singleLine = false, minLines = 3, maxChars = Validators.NOTE_MAX
            )
            GradientButton("Guardar registro", icon = Icons.Outlined.Check, onClick = {
                vm.save {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSaved()
                }
            })
        }
    }
}
