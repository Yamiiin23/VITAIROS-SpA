package cl.vitairos.vitairosm.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.vitairos.vitairosm.ui.components.ConnectivityBanner
import cl.vitairos.vitairosm.ui.screens.AgendaScreen
import cl.vitairos.vitairosm.ui.screens.AppointmentFormScreen
import cl.vitairos.vitairosm.ui.screens.HelpScreen
import cl.vitairos.vitairosm.ui.screens.HistoryScreen
import cl.vitairos.vitairosm.ui.screens.HomeScreen
import cl.vitairos.vitairosm.ui.screens.LoginScreen
import cl.vitairos.vitairosm.ui.screens.MoodFormScreen
import cl.vitairos.vitairosm.util.ConnectivityObserver
import cl.vitairos.vitairosm.viewmodel.AppViewModelProvider
import cl.vitairos.vitairosm.viewmodel.AppointmentViewModel
import cl.vitairos.vitairosm.viewmodel.AuthViewModel
import cl.vitairos.vitairosm.viewmodel.MoodViewModel
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val HISTORY = "history"
    const val AGENDA = "agenda"
    const val HELP = "help"
    const val NEW_MOOD = "mood/new"
    const val NEW_APPOINTMENT = "agenda/new"
}

private data class BottomItem(val route: String, val label: String, val icon: ImageVector)

private val bottomItems = listOf(
    BottomItem(Routes.HOME, "Inicio", Icons.Outlined.Home),
    BottomItem(Routes.HISTORY, "Registros", Icons.Outlined.History),
    BottomItem(Routes.AGENDA, "Agenda", Icons.Outlined.CalendarMonth),
    BottomItem(Routes.HELP, "Ayuda", Icons.Outlined.Shield)
)

private fun NavHostController.goTopLevel(route: String) = navigate(route) {
    popUpTo(Routes.HOME) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

@Composable
fun VitairosAppRoot() {
    val context = LocalContext.current
    val nav = rememberNavController()
    val auth: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val moodVm: MoodViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val apptVm: AppointmentViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val user by auth.user.collectAsStateWithLifecycle()
    val moods by moodVm.entries.collectAsStateWithLifecycle()
    val appointments by apptVm.appointments.collectAsStateWithLifecycle()
    val online by remember { ConnectivityObserver(context).isOnline }.collectAsStateWithLifecycle(initialValue = true)

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showMessage: (String) -> Unit = { msg -> scope.launch { snackbar.showSnackbar(msg) } }

    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val start = remember { if (auth.user.value != null) Routes.HOME else Routes.LOGIN }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            if (bottomItems.any { it.route == route }) {
                NavigationBar(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface) {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = route == item.route,
                            onClick = { nav.goTopLevel(item.route) },
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            ConnectivityBanner(offline = !online)
            NavHost(
                navController = nav,
                startDestination = start,
                modifier = Modifier.weight(1f),
                enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 10 } },
                exitTransition = { fadeOut(tween(200)) },
                popEnterTransition = { fadeIn(tween(300)) },
                popExitTransition = { fadeOut(tween(200)) }
            ) {
                composable(Routes.LOGIN) {
                    LoginScreen(auth) { nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } }
                }
                composable(Routes.HOME) {
                    HomeScreen(
                        userName = user ?: "",
                        moods = moods,
                        appointments = appointments,
                        onNewMood = { nav.navigate(Routes.NEW_MOOD) },
                        onAgenda = { nav.goTopLevel(Routes.AGENDA) },
                        onNewAppointment = { nav.navigate(Routes.NEW_APPOINTMENT) },
                        onHelp = { nav.goTopLevel(Routes.HELP) }
                    )
                }
                composable(Routes.HISTORY) {
                    HistoryScreen(moods, onNew = { nav.navigate(Routes.NEW_MOOD) }, onDelete = {
                        moodVm.delete(it)
                        showMessage("Registro eliminado")
                    })
                }
                composable(Routes.AGENDA) {
                    AgendaScreen(appointments, onNew = { nav.navigate(Routes.NEW_APPOINTMENT) }, onCancel = {
                        apptVm.cancel(it)
                        showMessage("Cita cancelada")
                    })
                }
                composable(Routes.HELP) {
                    HelpScreen(showMessage = showMessage, onLogout = {
                        auth.logout()
                        nav.navigate(Routes.LOGIN) { popUpTo(0) }
                    })
                }
                composable(Routes.NEW_MOOD) {
                    MoodFormScreen(moodVm, onBack = { nav.popBackStack() }, onSaved = {
                        nav.popBackStack()
                        showMessage("Registro guardado en tu historial")
                    })
                }
                composable(Routes.NEW_APPOINTMENT) {
                    AppointmentFormScreen(apptVm, onBack = { nav.popBackStack() }, onSaved = {
                        nav.popBackStack()
                        showMessage("Cita simulada agendada")
                    })
                }
            }
        }
    }
}
