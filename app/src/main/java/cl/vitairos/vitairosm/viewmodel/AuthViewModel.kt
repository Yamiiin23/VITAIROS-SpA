package cl.vitairos.vitairosm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.vitairos.vitairosm.data.session.SessionStore
import cl.vitairos.vitairosm.util.Validators
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val showPassword: Boolean = false,
    val loading: Boolean = false,
    val authError: String? = null
)

class AuthViewModel(private val session: SessionStore) : ViewModel() {
    companion object {
        const val DEMO_EMAIL = "demo@vitairos.test"
        const val DEMO_PASSWORD = "Demo1234"
        const val DEMO_NAME = "Camila (perfil ficticio)"
    }

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _user = MutableStateFlow(session.currentUser())
    val user: StateFlow<String?> = _user.asStateFlow()

    fun onEmail(v: String) = _state.update {
        it.copy(email = v, emailError = if (v.isBlank()) null else Validators.email(v), authError = null)
    }

    fun onPassword(v: String) = _state.update {
        it.copy(password = v, passwordError = if (v.isBlank()) null else Validators.password(v), authError = null)
    }

    fun togglePassword() = _state.update { it.copy(showPassword = !it.showPassword) }

    fun useDemoProfile() = _state.update {
        it.copy(email = DEMO_EMAIL, password = DEMO_PASSWORD, emailError = null, passwordError = null, authError = null)
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value
        val emailError = Validators.email(s.email)
        val passError = Validators.password(s.password)
        _state.update { it.copy(emailError = emailError, passwordError = passError) }
        if (emailError != null || passError != null) return

        viewModelScope.launch {
            _state.update { it.copy(loading = true, authError = null) }
            delay(700) // simula latencia de red
            if (s.email.trim().equals(DEMO_EMAIL, ignoreCase = true) && s.password == DEMO_PASSWORD) {
                session.login(DEMO_NAME)
                _user.value = DEMO_NAME
                _state.value = LoginUiState()
                onSuccess()
            } else {
                _state.update { it.copy(loading = false, authError = "Credenciales no válidas. Usa el perfil ficticio de prueba.") }
            }
        }
    }

    fun logout() {
        session.logout()
        _user.value = null
    }
}
