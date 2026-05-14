package com.example.jorozcomusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.jorozcomusicapp.navigation.AlbumDetailRoute
import com.example.jorozcomusicapp.navigation.HomeRoute
import com.example.jorozcomusicapp.ui.theme.JOrozcoMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JOrozcoMusicAppTheme {
                val navController = rememberNavController()

                // NavHost define el gráfico de navegación completo de la app.
                // startDestination es la pantalla que se muestra al iniciar.
                NavHost(
                    navController = navController,
                    startDestination = HomeRoute
                ) {
                    // Destino Home — se implementará en Fase 2–5
                    composable<HomeRoute> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Home Screen — Fase 2 próximamente")
                        }
                    }

                    // Destino Detail — se implementará en Fase 6–8
                    composable<AlbumDetailRoute> { backStackEntry ->
                        val route: AlbumDetailRoute = backStackEntry.toRoute()
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Detail Screen — Album ID: ${route.albumId}")
                        }
                    }
                }
            }
        }
    }
}
