package com.example.jorozcomusicapp.data.api

import com.example.jorozcomusicapp.data.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumApiService {

    // Obtiene la lista completa de álbumes para la pantalla Home.
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    // Obtiene un álbum específico para la pantalla Detail.
    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}
