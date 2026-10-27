package cl.vitairos.vitairosm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.vitairos.vitairosm.ui.navigation.VitairosAppRoot
import cl.vitairos.vitairosm.ui.theme.VitairosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { VitairosTheme { VitairosAppRoot() } }
    }
}
