package cl.vitairos.vitairosm.util

/**
 * Lógica de validación centralizada y desacoplada de la UI.
 * Cada función devuelve el mensaje de error o null si el valor es válido.
 */
object Validators {
    const val NOTE_MAX = 280
    const val REASON_MAX = 200
    private val EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun email(v: String): String? = when {
        v.isBlank() -> "Ingresa tu correo"
        !EMAIL.matches(v.trim()) -> "El formato del correo no es válido"
        else -> null
    }

    fun password(v: String): String? = when {
        v.isBlank() -> "Ingresa tu contraseña"
        v.length < 8 -> "Mínimo 8 caracteres"
        v.none { it.isDigit() } || v.none { it.isLetter() } -> "Combina letras y números"
        else -> null
    }

    fun mood(v: Int?): String? = if (v == null) "Selecciona cómo te sientes" else null

    fun domain(v: String?): String? = if (v.isNullOrBlank()) "Elige un ámbito de seguimiento" else null

    fun note(v: String): String? = when {
        v.isNotBlank() && v.trim().length < 5 -> "Escribe al menos 5 caracteres o deja la nota vacía"
        v.length > NOTE_MAX -> "Máximo $NOTE_MAX caracteres"
        else -> null
    }

    fun reason(v: String): String? = when {
        v.isBlank() -> "Cuéntanos brevemente el motivo"
        v.trim().length < 10 -> "Usa al menos 10 caracteres"
        v.length > REASON_MAX -> "Máximo $REASON_MAX caracteres"
        else -> null
    }

    fun selected(v: Any?, message: String): String? = if (v == null) message else null

    fun consent(accepted: Boolean): String? =
        if (accepted) null else "Debes aceptar para continuar"
}
