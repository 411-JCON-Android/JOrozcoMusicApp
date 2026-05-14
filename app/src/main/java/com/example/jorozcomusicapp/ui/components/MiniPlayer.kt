package com.example.jorozcomusicapp.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jorozcomusicapp.data.model.Album

// Mini reproductor fijo en la parte inferior de la pantalla.
// Recibe el álbum activo, el estado de reproducción y el callback de play/pause.
// No reproduce audio real — solo gestiona estado visual.
@Composable
fun MiniPlayer(
    album: Album?,
    isPlaying: Boolean,
    onPlayPause: () -> Unit
) {
    // Si no hay álbum cargado aún, no se muestra nada
    if (album == null) return

    // Padding exterior para separar la tarjeta de los bordes de la pantalla
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color = Color(0xFF1A0D2E))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen pequeña del álbum
        AsyncImage(
            model = buildImageRequest(LocalContext.current, album.image),
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Título y artista en el centro, con peso para empujar el botón a la derecha
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = album.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = album.artist,
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Botón circular blanco de play / pause
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                tint = Color(0xFF1A0D2E),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Reutiliza la misma lógica de headers que en HomeScreen para cargar imágenes de Wikipedia.
fun buildImageRequest(context: Context, url: String): ImageRequest {
    val secureUrl = if (url.startsWith("http://")) url.replace("http://", "https://") else url
    return ImageRequest.Builder(context)
        .data(secureUrl)
        .addHeader("User-Agent", "JOrozcoMusicApp/1.0 (Android)")
        .addHeader("Referer", "https://en.wikipedia.org/")
        .crossfade(true)
        .build()
}
