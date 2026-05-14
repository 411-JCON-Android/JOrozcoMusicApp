package com.example.jorozcomusicapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HomeBackground)
                    .statusBarsPadding()
            ) {
                DetailHeader(album = album!!, navController = navController)
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
                    icon = Icons.Default.Pause,
                    description = "Pause",
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
