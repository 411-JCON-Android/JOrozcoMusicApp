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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Color de fondo general de la pantalla Home
val HomeBackground = Color(0xFFEEE8FD)

@Composable
fun HomeScreen(navController: NavController) {
    // statusBarsPadding en la columna raíz para que el fondo lila aparezca
    // detrás de la barra de estado y la tarjeta empiece justo debajo.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBackground)
            .statusBarsPadding()
    ) {
        HomeHeader()
    }
}

// Tarjeta de bienvenida flotante con degradado morado y las 4 esquinas redondeadas.
@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Márgenes exteriores que dan el efecto de tarjeta flotante del mock
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7B5EA7),
                        Color(0xFF9B7FC7)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column {
            // Fila superior: ícono de menú a la izquierda, lupa a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Saludo y nombre del usuario
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
