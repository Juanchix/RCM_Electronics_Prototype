package com.example.rcm.views.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rcm.R

@Composable
fun WelcomeView(onEmployeeClick: () -> Unit, navController: NavController) {
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
                .padding(top = 200.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    // Dibujar la curva semicircular
                    moveTo(0f, size.height * 0.5f)
                    cubicTo(
                        size.width * 0.25f, size.height * 0.3f, // Primer punto de control
                        size.width * 0.75f, size.height * 0.3f, // Segundo punto de control
                        size.width, size.height * 0.5f // Punto final
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

        // Contenido
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Logo más grande
            Image(
                painter = painterResource(id = R.drawable.rcm_logo),
                contentDescription = "Logo RCM",
                modifier = Modifier
                    .size(300.dp) // Tamaño aumentado
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botones más grandes dentro de la sección azul
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 140.dp)
            ) {
                // Botón "Soy empleado"
                Button(
                    onClick = { onEmployeeClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Más ancho
                        .height(60.dp) // Más alto
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Soy empleado",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { navController.navigate("cliente")
                    }, // Navegar a la pantalla "cliente"
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057FF)),
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Soy cliente",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

