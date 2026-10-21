package com.electromechanics.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.electromechanics.app.ui.screen.*
import com.electromechanics.app.ui.viewmodel.AuthViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CONTROL_JORNADA = "control_jornada"
    const val AGENDA_SERVICIOS = "agenda_servicios"
    const val HISTORIAL_JORNADAS = "historial_jornadas"
    const val SUCURSALES_MAPA = "sucursales_mapa"
    const val NOTIFICACIONES = "notificaciones"
    const val PERFIL_EMPLEADO = "perfil_empleado"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val currentUserName = authViewModel.uiState.usuarioLogueado?.nombreCompleto ?: "Invitado"
    val currentUserId = authViewModel.uiState.usuarioLogueado?.id ?: "default_user"

    val logoutAction: () -> Unit = {
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.HOME) { inclusive = true }
        }
    }

    val homeAction: () -> Unit = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userName = currentUserName,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = logoutAction
            )
        }

        composable(Routes.CONTROL_JORNADA) {
            ControlJornadaScreen(
                userName = currentUserName,
                userId = currentUserId,
                onNavigateHome = homeAction,
                onLogout = logoutAction
            )
        }
        composable(Routes.AGENDA_SERVICIOS) {
            AgendaServiciosScreen(
                userName = currentUserName,
                onNavigateHome = homeAction,
                onLogout = logoutAction
            )
        }
        composable(Routes.HISTORIAL_JORNADAS) {
            HistorialJornadasScreen(userName = currentUserName, userId = currentUserId, onNavigateHome = homeAction)
        }
        composable(Routes.SUCURSALES_MAPA) {
            SucursalesMapaScreen(userName = currentUserName, onNavigateHome = homeAction)
        }
        composable(Routes.NOTIFICACIONES) {
            NotificacionesScreen(userName = currentUserName, onNavigateHome = homeAction)
        }
        composable(Routes.PERFIL_EMPLEADO) {
            PerfilEmpleadoScreen(
                user = authViewModel.uiState.usuarioLogueado,
                onNavigateHome = homeAction
            )
        }
    }
}