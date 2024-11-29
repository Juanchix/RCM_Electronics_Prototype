package com.example.rcm.views.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rcm.R
import com.example.rcm.views.navbar.NavBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeView(navController: NavController, context: Context) {
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    // Obtener la ubicación actual
    LaunchedEffect(Unit) {
        currentLocation = getCurrentLocation(fusedLocationClient)
    }

    // Obtener la fecha actual
    val currentDate = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es"))
    ).replaceFirstChar { it.uppercase() }

    Scaffold(
        bottomBar = { NavBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Habilitar scroll
            ) {
                // Header con bordes inferiores redondeados
                // Header con bordes redondeados
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0057FF), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = currentDate,
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "BIENVENIDO, ISAAC",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Personal técnico",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { /* Acción de notificación */ }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido con padding
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // Padding original para los demás componentes
                ) {
                    // Visitas pendientes
                    Text(
                        text = "Visitas pendientes",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            VisitItem("Terpel Aeropuerto", "25 nov", "10:00 am")
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            VisitItem("Terpel Pontevedra", "25 nov", "5:00 pm")
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            VisitItem("Meals de Colombia", "26 nov", "9:00 am")
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            VisitItem("Mazda Morato", "27 nov", "8:30 am")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ubicación actual
                    Text(
                        text = "Ubicación actual",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 22.sp),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp), // Aumentar altura del mapa
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
                        val cameraPositionState = rememberCameraPositionState()
                        var currentLocation by remember { mutableStateOf<LatLng?>(null) }

                        // Actualizar la ubicación y el estado de la cámara
                        LaunchedEffect(Unit) {
                            currentLocation = getCurrentLocation(fusedLocationClient)
                            currentLocation?.let {
                                cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                                    it,
                                    15f // Zoom inicial más cercano
                                )
                            }
                        }

                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState
                        ) {
                            currentLocation?.let {
                                Marker(
                                    state = MarkerState(position = it),
                                    title = "Tu ubicación actual"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Sede: Bogotá Morato",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            // Ícono de WhatsApp en la esquina inferior derecha
            FloatingActionButton(
                onClick = { /* Acción de WhatsApp */ },
                containerColor = Color(0xFF25D366),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(64.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.whatsapp_logo),
                    contentDescription = "WhatsApp",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient): LatLng? {
    return try {
        val location = fusedLocationClient.lastLocation.await()
        location?.let {
            LatLng(it.latitude, it.longitude)
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun VisitItem(title: String, date: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = date, style = MaterialTheme.typography.bodySmall)
            Text(text = time, style = MaterialTheme.typography.bodySmall)
        }
    }
}
