package cl.vitairos.vitairosm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vitairos.vitairosm.data.local.Appointment
import cl.vitairos.vitairosm.data.local.AppointmentStatus
import cl.vitairos.vitairosm.data.repository.VitairosRepository
import cl.vitairos.vitairosm.model.Professional
import cl.vitairos.vitairosm.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppointmentFormState(
    val professional: Professional? = null,
    val date: String? = null,
    val time: String? = null,
    val modality: String = "Videollamada",
    val reason: String = "",
    val consent: Boolean = false,
    val reminder: Boolean = false,
    val professionalError: String? = null,
    val dateError: String? = null,
    val timeError: String? = null,
    val reasonError: String? = null,
    val consentError: String? = null
)

class AppointmentViewModel(private val repo: VitairosRepository) : ViewModel() {
    val appointments: StateFlow<List<Appointment>> =
        repo.appointments.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _form = MutableStateFlow(AppointmentFormState())
    val form: StateFlow<AppointmentFormState> = _form.asStateFlow()

    fun onProfessional(p: Professional) = _form.update { it.copy(professional = p, professionalError = null) }
    fun onDate(v: String) = _form.update { it.copy(date = v, dateError = null) }
    fun onTime(v: String) = _form.update { it.copy(time = v, timeError = null) }
    fun onModality(v: String) = _form.update { it.copy(modality = v) }
    fun onReason(v: String) = _form.update {
        val text = v.take(Validators.REASON_MAX)
        it.copy(reason = text, reasonError = if (text.isBlank()) null else Validators.reason(text))
    }
    fun onConsent(v: Boolean) = _form.update { it.copy(consent = v, consentError = null) }
    fun onReminder(v: Boolean) = _form.update { it.copy(reminder = v) }
    fun resetForm() { _form.value = AppointmentFormState() }

    /** onSaved recibe la cita creada y si el usuario pidió recordatorio. */
    fun save(onSaved: (Appointment, Boolean) -> Unit) {
        val s = _form.value
        val checked = s.copy(
            professionalError = Validators.selected(s.professional, "Elige un profesional"),
            dateError = Validators.selected(s.date, "Elige una fecha"),
            timeError = Validators.selected(s.time, "Elige un horario"),
            reasonError = Validators.reason(s.reason),
            consentError = Validators.consent(s.consent)
        )
        _form.value = checked
        if (listOf(checked.professionalError, checked.dateError, checked.timeError, checked.reasonError, checked.consentError).any { it != null }) return

        val appointment = Appointment(
            professional = s.professional!!.name, specialty = s.professional.specialty,
            dateLabel = s.date!!, timeLabel = s.time!!, modality = s.modality,
            reason = s.reason.trim(), status = AppointmentStatus.SCHEDULED,
            createdAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repo.addAppointment(appointment)
            _form.value = AppointmentFormState()
            onSaved(appointment, s.reminder)
        }
    }

    fun cancel(id: Long) = viewModelScope.launch { repo.cancelAppointment(id) }
}
