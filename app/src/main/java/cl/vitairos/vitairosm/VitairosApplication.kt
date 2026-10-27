package cl.vitairos.vitairosm

import android.app.Application
import cl.vitairos.vitairosm.data.AppContainer
import cl.vitairos.vitairosm.util.Notifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class VitairosApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        Notifier.createChannel(this)
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch { container.repository.seedIfEmpty() }
    }
}
