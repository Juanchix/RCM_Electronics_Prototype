package com.example.rcm.views.acta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rcm.views.navbar.NavBar
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActaDetailView(
    navController: NavController,
    actaTitle: String,
    estado: String,
    tecnico: String,
    fecha: String,
    notas: String
) {
    // Lista de ubicaciones según el título del acta
    val ubicaciones = mapOf(
        "Terpel Aeropuerto" to LatLng(4.7019, -74.1472),
        "Terpel Serviruedas" to LatLng(4.6948, -74.0683),
        "Caracol" to LatLng(4.6944, -74.0727),
        "Toyota Morato" to LatLng(4.6951, -74.0734),
        "Empresa XYZ" to LatLng(4.6543, -74.0588),
        "Uniandes" to LatLng(4.6030, -74.0652)
    )

    // Asignar ubicación específica
    val ubicacionActa = ubicaciones[actaTitle] ?: LatLng(0.0, 0.0)

    // Información adicional para el acta
    val equiposData = listOf(
        "Controlador AC-215" to "$500",
        "Cámara DS-2CD" to "$800"
    )
    val segundaVisita = "Sí"
    val detallesVisita = "Instalación exitosa. Todo en orden."

    Scaffold(
        bottomBar = { NavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = com.example.rcm.R.drawable.arrow_back_24),
                        contentDescription = "Volver"
                    )
                }
                Text(
                    text = "$actaTitle",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estado
            Text("Estado del acta:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(estado, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Mapa de ubicación
            Text("Ubicación de la actividad:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                val cameraPositionState = rememberCameraPositionState {
                    position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                        ubicacionActa, 15f
                    )
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = ubicacionActa),
                        title = actaTitle
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Técnico encargado
            Text("Técnico encargado:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(tecnico, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha del acta
            Text("Fecha de la actividad:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(fecha, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Equipos implicados
            Text("Equipos implicados:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            equiposData.forEach { (nombre, costo) ->
                Text("$nombre: $costo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Segunda visita
            Text("¿Se requiere una segunda visita?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(segundaVisita, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Detalles de la visita
            Text("Detalles de la visita:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(detallesVisita, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de acción
            Button(
                onClick = { /* Acción para compartir o guardar */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Compartir Acta")
            }
        }
    }
}
