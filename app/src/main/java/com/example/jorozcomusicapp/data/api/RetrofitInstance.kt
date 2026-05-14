package com.example.jorozcomusicapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton que provee la instancia de Retrofit configurada con la URL base de la API.
// Se usa `by lazy` para inicializarla solo cuando se necesita por primera vez.
object RetrofitInstance {

    private const val BASE_URL = "https://musicapi.pjasoft.com/"

    val api: AlbumApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlbumApiService::class.java)
    }
}
