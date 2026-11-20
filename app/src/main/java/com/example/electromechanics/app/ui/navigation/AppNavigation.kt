package com.electromechanics.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.MensajeToast
import com.electromechanics.app.ui.screen.*
import com.electromechanics.app.ui.viewmodel.AuthViewModel
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val PERFIL = "perfil"
    const val HISTORIAL_JORNADAS = "historial_jornadas" // ambos roles (distinto contenido)
    // Técnico
    const val CONTROL_JORNADA = "control_jornada"
    const val MIS_ORDENES = "mis_ordenes"
    // Supervisor
    const val TECNICOS = "tecnicos"
    const val ORDENES_SUP = "ordenes_supervisor"
    const val ACTIVIDADES = "actividades"
}

@Composable
private fun ConRol(
    usuario: Empleado?,
    permitido: (Empleado) -> Boolean,
    onDenegado: () -> Unit,
    contenido: @Composable (Empleado) -> Unit
) {
    if (usuario != null) {
        if (permitido(usuario)) {
            contenido(usuario)
        } else {
            LaunchedEffect(Unit) { onDenegado() }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authVm: AuthViewModel = viewModel()
    val opsVm: OperacionesViewModel = viewModel()
    val usuario by authVm.usuario.observeAsState()
    val sesionCargada by authVm.sesionCargada.observeAsState(false)

    MensajeToast(opsVm)

    val homeAction: () -> Unit = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) { inclusive = true }
        }
    }

    val logoutAction: () -> Unit = {
        authVm.logout()
        navController.navigate(Routes.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    if (!sesionCargada) return

    val inicio = remember { if (authVm.usuario.value != null) Routes.HOME else Routes.LOGIN }

    NavHost(navController = navController, startDestination = inicio) {

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authVm,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            ConRol(usuario, { true }, homeAction) { u ->
                HomeScreen(
                    usuario = u,
                    onNavigate = { route -> navController.navigate(route) },
                    onLogout = logoutAction
                )
            }
        }

        composable(Routes.PERFIL) {
            ConRol(usuario, { true }, homeAction) { u ->
                PerfilEmpleadoScreen(usuario = u, onNavigateHome = homeAction)
            }
        }

        composable(Routes.HISTORIAL_JORNADAS) {
            ConRol(usuario, { true }, homeAction) { u ->
                HistorialJornadasScreen(usuario = u, vm = opsVm, onNavigateHome = homeAction)
            }
        }

        // ---------- Solo TECNICO ----------
        composable(Routes.CONTROL_JORNADA) {
            ConRol(usuario, { it.esTecnico }, homeAction) { u ->
                ControlJornadaScreen(usuario = u, vm = opsVm, onNavigateHome = homeAction, onLogout = logoutAction)
            }
        }

        composable(Routes.MIS_ORDENES) {
            ConRol(usuario, { it.esTecnico }, homeAction) { u ->
                MisOrdenesScreen(usuario = u, vm = opsVm, onNavigateHome = homeAction, onLogout = logoutAction)
            }
        }

        // ---------- Solo SUPERVISOR ----------
        composable(Routes.TECNICOS) {
            ConRol(usuario, { it.esSupervisor }, homeAction) { u ->
                TecnicosScreen(usuario = u, vm = opsVm, onNavigateHome = homeAction, onLogout = logoutAction)
            }
        }

        composable(Routes.ORDENES_SUP) {
            ConRol(usuario, { it.esSupervisor }, homeAction) { u ->
                OrdenesSupervisorScreen(usuario = u, vm = opsVm, onNavigateHome = homeAction, onLogout = logoutAction)
            }
        }

        composable(Routes.ACTIVIDADES) {
            ConRol(usuario, { it.esSupervisor }, homeAction) { u ->
                ActividadesScreen(usuario = u, vm = opsVm, onNavigateHome = homeAction)
            }
        }
    }
}
