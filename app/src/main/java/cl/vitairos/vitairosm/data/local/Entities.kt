package cl.vitairos.vitairosm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Registro emocional (datos sintéticos). mood: 1 (muy bajo) a 5 (muy bien). */
@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mood: Int,
    val domain: String,
    val note: String,
    val createdAt: Long
)

/** Cita ficticia con profesional simulado. */
@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val professional: String,
    val specialty: String,
    val dateLabel: String,
    val timeLabel: String,
    val modality: String,
    val reason: String,
    val status: String,
    val createdAt: Long
)

object AppointmentStatus {
    const val SCHEDULED = "Programada"
    const val DONE = "Realizada"
    const val CANCELLED = "Cancelada"
}
