package com.example.jorozcomusicapp.data.model

import kotlinx.serialization.Serializable

// Modelo principal que representa un álbum de la API.
// Gson (Retrofit) usa reflexión para deserializar JSON;
// @Serializable es requerido por Navigation 2.8 para pasar objetos entre rutas.
@Serializable
data class Album(
    val id: String,           // MongoDB ObjectId — llega como string
    val title: String,
    val artist: String,
    val image: String = "",   // URL de la portada del álbum
    val description: String? = null
)
