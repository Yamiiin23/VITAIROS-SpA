package cl.vitairos.vitairosm.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.vitairos.vitairosm.VitairosApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { AuthViewModel(app().container.session) }
        initializer { MoodViewModel(app().container.repository) }
        initializer { AppointmentViewModel(app().container.repository) }
    }
}

private fun CreationExtras.app(): VitairosApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VitairosApplication
