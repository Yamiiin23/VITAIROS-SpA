package cl.vitairos.vitairosm.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    fun dateTime(millis: Long): String =
        SimpleDateFormat("d MMM yyyy, HH:mm", Locale("es", "CL")).format(Date(millis))
}
