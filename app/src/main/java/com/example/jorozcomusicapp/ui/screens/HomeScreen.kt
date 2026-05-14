package com.example.jorozcomusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jorozcomusicapp.data.api.RetrofitInstance
import com.example.jorozcomusicapp.data.model.Album
import com.example.jorozcomusicapp.navigation.AlbumDetailRoute

// Color de fondo general de la pantalla Home
val HomeBackground = Color(0xFFEEE8FD)
val PurpleAccent = Color(0xFF7B5EA7)

@Composable
fun HomeScreen(navController: NavController) {
    // Estado compartido de álbumes: se usa tanto en el carrusel como en Recently Played
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect(Unit) se ejecuta una sola vez al montar la pantalla.
    // withContext(IO) garantiza que la llamada de red no bloquee el hilo principal.
    LaunchedEffect(Unit) {
        try {
            albums = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getAlbums()
            }
        } catch (e: Exception) {
            // Muestra el mensaje real para facilitar el diagnóstico
            error = e::class.simpleName + ": " + e.message
        } finally {
            isLoading = false
        }
    }

    // Un único LazyColumn evita conflictos de scroll con el LazyRow de Albums.
    // Cada sección es un item; los álbumes de Recently Played se renderizan con items().
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBackground)
            .statusBarsPadding()
    ) {
        item { HomeHeader() }
        item { AlbumsSection(albums, isLoading, error, navController) }
        item { RecentlyPlayedHeader() }
        items(albums) { album ->
            RecentlyPlayedItem(album) {
                navController.navigate(AlbumDetailRoute(album.id))
            }
        }
    }
}

// ─── Header ──────────────────────────────────────────────────────────────────

@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF7B5EA7), Color(0xFF9B7FC7))
                )
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Good Morning!",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "Juan Orozco",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ─── Sección Albums ───────────────────────────────────────────────────────────

@Composable
fun AlbumsSection(
    albums: List<Album>,
    isLoading: Boolean,
    error: String?,
    navController: NavController
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        // Fila de título y enlace "See more"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Albums", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "See more", fontSize = 14.sp, color = PurpleAccent)
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PurpleAccent)
                }
            }
            error != null -> {
                Text(
                    text = "Error al cargar álbumes",
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(albums) { album ->
                        AlbumCarouselCard(album = album) {
                            navController.navigate(AlbumDetailRoute(album.id))
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta grande del carrusel: imagen de fondo y recuadro morado translúcido
// en la parte inferior con título, artista y botón de play.
@Composable
fun AlbumCarouselCard(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Imagen de fondo del álbum
            AsyncImage(
                model = rememberImageRequest(LocalContext.current, album.image),
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Recuadro morado translúcido flotante con esquinas redondeadas,
            // separado de los bordes de la tarjeta por un margen (padding).
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xBB6B4E97))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = album.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = album.artist,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón circular de play
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reproducir",
                            tint = PurpleAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─── Recently Played ──────────────────────────────────────────────────────────

// Fila de título "Recently Played" y enlace "See more".
@Composable
fun RecentlyPlayedHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Recently Played", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = "See more", fontSize = 14.sp, color = PurpleAccent)
    }
}

// Ítem individual de la lista Recently Played: card blanca con imagen, título, artista y menú.
@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen cuadrada redondeada a la izquierda
            AsyncImage(
                model = rememberImageRequest(LocalContext.current, album.image),
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Título y subtítulo "Artista • Popular Song"
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = album.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = "${album.artist} • Popular Song",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            // Ícono de tres puntos a la derecha
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Helper de imagen ─────────────────────────────────────────────────────────

// Construye un ImageRequest con los headers que Wikipedia requiere y fuerza HTTPS
// para evitar que Android 9+ bloquee URLs con http://.
fun rememberImageRequest(context: Context, url: String): ImageRequest {
    val secureUrl = if (url.startsWith("http://")) url.replace("http://", "https://") else url
    return ImageRequest.Builder(context)
        .data(secureUrl)
        .addHeader("User-Agent", "JOrozcoMusicApp/1.0 (Android)")
        .addHeader("Referer", "https://en.wikipedia.org/")
        .crossfade(true)
        .build()
}
