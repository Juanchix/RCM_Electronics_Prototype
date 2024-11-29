package com.example.rcm.views.cliente

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rcm.R

@Composable
fun ClienteMainView(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo blanco
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        )

        // Sección azul con forma semicircular
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, size.height * 0.35f) // Ajusta la altura del área azul
                    cubicTo(
                        size.width * 0.25f, size.height * 0.15f, // Primer punto de control
                        size.width * 0.75f, size.height * 0.15f, // Segundo punto de control
                        size.width, size.height * 0.35f // Punto final
                    )
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(
                    path = path,
                    color = Color(0xFF0057FF)
                )
            }
        }

        // Botón de retroceso
        IconButton(
            onClick = { navController.popBackStack() }, // Navegar hacia atrás
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_24),
                contentDescription = "Volver"
            )
        }


        // Contenido principal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.rcm_logo),
                contentDescription = "Logo RCM",
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(150.dp))


            Text(
                text = "¡Esta funcionalidad aún está en desarrollo!",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 22.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Gracias por tu paciencia mientras trabajamos en esta funcionalidad.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
