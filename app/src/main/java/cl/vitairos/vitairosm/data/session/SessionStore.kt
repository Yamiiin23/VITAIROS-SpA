package cl.vitairos.vitairosm.data.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Gestor de sesión persistente mediante SharedPreferences.
 */
class SessionStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("vitairos_session", Context.MODE_PRIVATE)

    fun currentUser(): String? = prefs.getString(KEY_USER_NAME, null)

    fun login(userName: String) {
        prefs.edit { putString(KEY_USER_NAME, userName) }
    }

    fun logout() {
        prefs.edit { remove(KEY_USER_NAME) }
    }

    companion object {
        private const val KEY_USER_NAME = "user_name"
    }
}
