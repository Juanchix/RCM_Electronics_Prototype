package com.example.rcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rcm.ui.theme.RCMTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rcm.views.acta.ActaFutureView
import com.example.rcm.views.acta.ActaDetailView
import com.example.rcm.views.acta.ActaView
import com.example.rcm.views.cliente.ClienteMainView
import com.example.rcm.views.home.HomeView
import com.example.rcm.views.login.LoginView
import com.example.rcm.views.login.WelcomeView
import com.example.rcm.views.profile.ProfileView
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RCMTheme {
                RCMApp(context = this) // Pasar contexto al NavHost
            }
        }
    }
}

@Composable
fun RCMApp(context: android.content.Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        // Pantalla de bienvenida
        composable("start") {
            WelcomeView(
                onEmployeeClick = { navController.navigate("login_employee") }, navController = navController,
            )
        }
        composable("cliente") {
            ClienteMainView(navController = navController)
        }
        composable("actas") {
            ActaView(navController = navController)
        }

        // Pantalla de login para empleados
        composable("login_employee") {
            LoginView(onLoginSuccess = { navController.navigate("home")}, navController = navController,
            )
        }

        // Pantalla principal (Home)
        composable("home") {
            HomeView(navController = navController, context = context)
        }

// Ruta para actas futuras
        composable("acta_futura/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")?.replace("_", " ") ?: "Acta Futura"
            ActaFutureView(
                navController = navController,
                context = context,
                actaTitle = title
            )
        }

        composable("profile") {
            ProfileView(navController = navController)
        }

// Ruta para actas pasadas (detalle)
        composable("detalle_acta/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")?.replace("_", " ") ?: "Acta"
            ActaDetailView(
                navController = navController,
                actaTitle = title,
                estado = "Completada",
                tecnico = "Yair Isaac",
                fecha = "8-11-24 12:13pm",
                notas = "Notas detalladas del acta...",
            )
        }
    }
}
