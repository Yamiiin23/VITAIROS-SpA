package cl.vitairos.vitairosm.data.repository

import cl.vitairos.vitairosm.data.local.Appointment
import cl.vitairos.vitairosm.data.local.AppointmentStatus
import cl.vitairos.vitairosm.data.local.MoodEntry
import cl.vitairos.vitairosm.data.local.VitairosDao
import cl.vitairos.vitairosm.model.Catalog

/** Única fuente de datos de la UI. */
class VitairosRepository(private val dao: VitairosDao) {
    val moods = dao.observeMoods()
    val appointments = dao.observeAppointments()

    suspend fun addMood(entry: MoodEntry) = dao.insertMood(entry)
    suspend fun deleteMood(entry: MoodEntry) = dao.deleteMood(entry)
    suspend fun addAppointment(a: Appointment) = dao.insertAppointment(a)
    suspend fun cancelAppointment(id: Long) = dao.updateStatus(id, AppointmentStatus.CANCELLED)

    /** Dataset sintético inicial (sin datos reales). */
    suspend fun seedIfEmpty() {
        val now = System.currentTimeMillis()
        val day = 86_400_000L
        if (dao.countMoods() == 0) {
            dao.insertMood(MoodEntry(mood = 3, domain = Catalog.domains[2], note = "Semana con muchas entregas (dato de prueba).", createdAt = now - 3 * day))
            dao.insertMood(MoodEntry(mood = 4, domain = Catalog.domains[1], note = "Dormí mejor esta noche (dato de prueba).", createdAt = now - 2 * day))
            dao.insertMood(MoodEntry(mood = 4, domain = Catalog.domains[4], note = "", createdAt = now - day))
        }
        if (dao.countAppointments() == 0) {
            val p = Catalog.professionals[0]
            dao.insertAppointment(
                Appointment(
                    professional = p.name, specialty = p.specialty, dateLabel = "Próxima semana",
                    timeLabel = "11:30", modality = "Videollamada",
                    reason = "Seguimiento de prueba del plan de descanso.",
                    status = AppointmentStatus.SCHEDULED, createdAt = now - day
                )
            )
        }
    }
}
