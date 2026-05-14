package com.example.jorozcomusicapp.data.model

import kotlinx.serialization.Serializable

// Modelo principal que representa un álbum de la API.
// Gson (Retrofit) usa reflexión para deserializar JSON;
// @Serializable es requerido por Navigation 2.8 para pasar objetos entre rutas.
@Serializable
data class Album(
    val id: Int,
    val title: String,
    val artist: String,
    val cover: String,
    val description: String? = null
)
