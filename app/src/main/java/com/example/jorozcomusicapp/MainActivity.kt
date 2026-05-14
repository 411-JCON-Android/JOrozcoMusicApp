package com.example.jorozcomusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.jorozcomusicapp.navigation.AlbumDetailRoute
import com.example.jorozcomusicapp.navigation.HomeRoute
import com.example.jorozcomusicapp.ui.screens.DetailScreen
import com.example.jorozcomusicapp.ui.screens.HomeScreen
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
                    composable<HomeRoute> {
                        HomeScreen(navController)
                    }

                    composable<AlbumDetailRoute> { backStackEntry ->
                        val route: AlbumDetailRoute = backStackEntry.toRoute()
                        DetailScreen(albumId = route.albumId, navController = navController)
                    }
                }
            }
        }
    }
}
