package com.example.jorozcomusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jorozcomusicapp.data.api.RetrofitInstance
import com.example.jorozcomusicapp.ui.components.MiniPlayer
import com.example.jorozcomusicapp.data.model.Album
import com.example.jorozcomusicapp.ui.components.buildImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DetailScreen(albumId: String, navController: NavController) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Carga el detalle del álbum al montar la pantalla
    LaunchedEffect(albumId) {
        try {
            album = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getAlbumById(albumId)
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleAccent)
            }
        }
        error != null || album == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error al cargar el álbum", color = Color.Red)
            }
        }
        else -> {
            val currentAlbum = album!!
            var isPlaying by remember { mutableStateOf(false) }

            Scaffold(
                containerColor = HomeBackground,
                bottomBar = {
                    MiniPlayer(
                        album = currentAlbum,
                        isPlaying = isPlaying,
                        onPlayPause = { isPlaying = !isPlaying }
                    )
                }
            ) { innerPadding ->
                // LazyColumn único para evitar scroll anidado
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(HomeBackground)
                        .statusBarsPadding()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    item { DetailHeader(album = currentAlbum, navController = navController) }
                    item { AboutCard(album = currentAlbum) }
                    item { ArtistChip(artist = currentAlbum.artist) }
                    // 10 pistas ficticias generadas a partir del título del álbum
                    items(10) { index ->
                        TrackItem(
                            trackTitle = "${currentAlbum.title} • Track ${index + 1}",
                            artist = currentAlbum.artist,
                            imageUrl = currentAlbum.image
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

// ─── Detail Header ────────────────────────────────────────────────────────────

// Sección superior del detalle: tarjeta flotante con imagen, scrim morado,
// botones de navegación, título, artista y botones de play/pause.
@Composable
fun DetailHeader(album: Album, navController: NavController) {
    // Box exterior: solo aplica los márgenes para el efecto de tarjeta flotante
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
    // Box interior: tiene dimensiones definidas antes del clip, para que funcione correctamente
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        // Imagen del álbum ocupando todo el box
        AsyncImage(
            model = buildImageRequest(LocalContext.current, album.image),
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Scrim morado degradado de abajo hacia arriba para legibilidad del texto
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xAA5B3E8F),
                            Color(0xEE3D2070)
                        )
                    )
                )
        )

        // Fila superior: botón atrás (izq) y corazón (der) con padding de status bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para regresar a Home
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorito",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Parte inferior: título, artista y botones de play/shuffle
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = album.title,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = album.artist,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de Play y Pause
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailActionButton(
                    icon = Icons.Default.PlayArrow,
                    description = "Play",
                    filled = true
                )
                DetailActionButton(
                    icon = Icons.Default.Shuffle,
                    description = "Shuffle",
                    filled = false
                )
            }
        }
    } // cierra Box interior (con clip y height)
    } // cierra Box exterior (con padding de márgenes)
}

// Botón circular de acción: relleno morado (play) o blanco (pause).
@Composable
fun DetailActionButton(icon: ImageVector, description: String, filled: Boolean) {
    val bgColor = if (filled) PurpleAccent else Color.White
    val iconColor = if (filled) Color.White else Color.Black.copy(alpha = 0.8f)

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

// ─── About Card ───────────────────────────────────────────────────────────────

// Tarjeta blanca flotante con la descripción del álbum, separada de los bordes.
@Composable
fun AboutCard(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "About this album",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleAccent
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = album.description ?: "No hay descripción disponible para este álbum.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

// ─── Artist Chip ──────────────────────────────────────────────────────────────

// Etiqueta con borde redondeado que muestra "Artist:" en morado y el nombre en gris.
@Composable
fun ArtistChip(artist: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = PurpleAccent,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // AnnotatedString permite mezclar estilos dentro del mismo Text
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PurpleAccent)) {
                    append("Artist: ")
                }
                withStyle(SpanStyle(color = Color.Gray)) {
                    append(artist)
                }
            },
            fontSize = 14.sp
        )
    }
}

// ─── Track Item ───────────────────────────────────────────────────────────────

// Ítem de pista ficticia: misma estructura que RecentlyPlayedItem del Home.
@Composable
fun TrackItem(trackTitle: String, artist: String, imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp),
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
            AsyncImage(
                model = buildImageRequest(LocalContext.current, imageUrl),
                contentDescription = trackTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trackTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = artist,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
