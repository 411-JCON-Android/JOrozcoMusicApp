package com.example.jorozcomusicapp.navigation

import kotlinx.serialization.Serializable

// Rutas de Navigation Compose 2.8 con type-safety via kotlinx.serialization.
// Cada objeto/data class representa una destino en el NavGraph.

@Serializable
object HomeRoute

@Serializable
data class AlbumDetailRoute(val albumId: String)
