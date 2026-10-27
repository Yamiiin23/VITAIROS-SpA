package cl.vitairos.vitairosm.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Professional(
    val name: String,
    val specialty: String,
)

object Catalog {
    val moodLabels = listOf("Muy bajo", "Bajo", "Neutro", "Bien", "Excelente")

    val domains = listOf(
        "Académico / Laboral",
        "Sueño y descanso",
        "Relaciones e interacciones",
        "Ansiedad o estrés",
        "Ánimo general",
    )

    val professionals = listOf(
        Professional("Dra. Sofia Morales", "Psicóloga clínica"),
        Professional("Dr. Felipe Araya", "Psiquiatra"),
        Professional("Lic. Carmen Gloria Silva", "Terapeuta ocupacional"),
        Professional("Mg. Esteban Rivas", "Psicólogo orientador"),
    )

    val times = listOf("09:00", "10:30", "11:30", "15:00", "16:30", "18:00")

    val modalities = listOf("Videollamada", "Presencial (Centro Vitairos)", "Telefónica")

    fun nextDays(): List<String> {
        val sdf = SimpleDateFormat("EEE d MMM", Locale("es", "CL"))
        val cal = Calendar.getInstance()
        val list = mutableListOf<String>()
        for (i in 0 until 5) {
            val dateStr = sdf.format(cal.time).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "CL")) else it.toString() }
            val label = when (i) {
                0 -> "Hoy ($dateStr)"
                1 -> "Mañana ($dateStr)"
                else -> dateStr
            }
            list.add(label)
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }
}
