package cl.vitairos.vitairosm.data

import android.content.Context
import cl.vitairos.vitairosm.data.local.AppDatabase
import cl.vitairos.vitairosm.data.repository.VitairosRepository
import cl.vitairos.vitairosm.data.session.SessionStore

/**
 * Contenedor de inyección de dependencias manual a nivel de aplicación.
 */
class AppContainer(private val context: Context) {
    private val database: AppDatabase by lazy {
        AppDatabase.build(context)
    }

    val repository: VitairosRepository by lazy {
        VitairosRepository(database.dao())
    }

    val session: SessionStore by lazy {
        SessionStore(context)
    }
}
