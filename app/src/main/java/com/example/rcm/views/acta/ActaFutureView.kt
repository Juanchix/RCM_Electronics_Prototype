package com.example.rcm.views.acta

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActaFutureView(
    navController: NavController,
    context: Context,
    actaTitle: String
) {
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val currentDateTime = LocalDateTime.now()
    val formattedDateTime = currentDateTime.format(
        DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM yyyy, hh:mm a", Locale("es"))
    ).replaceFirstChar { it.uppercase() }
    val technicianName = "Isaac"
    val companyName = actaTitle.filter { it.isLetter() }.replaceFirstChar { it.uppercase() }
    val formattedActaTitle = actaTitle.lowercase().replace(" ", "_")
    val actaId = remember { (10000000..99999999).random() }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var tipoVisita by remember { mutableStateOf("") }
    var horasDuracion by remember { mutableStateOf("") }
    var minutosDuracion by remember { mutableStateOf("") }
    var equipos by remember { mutableStateOf(mutableListOf<Pair<String, String>>()) }
    var equipoSeleccionado by remember { mutableStateOf("") }
    var equipoCosto by remember { mutableStateOf("") }
    var segundaVisita by remember { mutableStateOf("") }

    val equiposOpciones = listOf("Laptop", "Servidor", "Impresora", "Router", "Switch")
    val opcionesSegundaVisita = listOf("Sí", "No")

    LaunchedEffect(Unit) {
        currentLocation = getCurrentLocation(fusedLocationClient)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        painter = painterResource(id = R.drawable.arrow_back_24),
                        contentDescription = "Volver"
                    )
                }
                Text(
                    text = "Acta: $formattedActaTitle",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información del encabezado
            Text("ID: $actaId", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Fecha y Hora: $formattedDateTime", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Información del técnico y empresa
            Text("Técnico: $technicianName", fontSize = 16.sp)
            Text("Empresa: $companyName", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Mapa de ubicación actual
            Text("Ubicación actual:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                val cameraPositionState = rememberCameraPositionState {
                    position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                        currentLocation ?: LatLng(0.0, 0.0),
                        15f
                    )
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    currentLocation?.let {
                        Marker(state = MarkerState(position = it), title = "Ubicación actual")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de visita
            Text("Tipo de Visita:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            var expanded by remember { mutableStateOf(false) }
            val opciones = listOf("Cotización", "Instalación", "Mantenimiento", "Reparación", "Control")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipoVisita,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione tipo de visita") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    opciones.forEach { opcion ->
                        DropdownMenuItem(
                            onClick = {
                                tipoVisita = opcion
                                expanded = false
                            },
                            text = { Text(opcion) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

// Duración de la visita (horas y minutos)
            Text("Duración de la visita (hh:mm):", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row {
                OutlinedTextField(
                    value = horasDuracion,
                    onValueChange = { horasDuracion = it.filter { char -> char.isDigit() }.take(2) },
                    label = { Text("Horas") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = minutosDuracion,
                    onValueChange = { minutosDuracion = it.filter { char -> char.isDigit() }.take(2) },
                    label = { Text("Minutos") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Equipos implicados
            Text("Equipos Implicados:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            var equiposExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = equiposExpanded,
                onExpandedChange = { equiposExpanded = !equiposExpanded }
            ) {
                OutlinedTextField(
                    value = equipoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione un equipo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = equiposExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = equiposExpanded,
                    onDismissRequest = { equiposExpanded = false }
                ) {
                    equiposOpciones.forEach { equipo ->
                        DropdownMenuItem(
                            onClick = {
                                equipoSeleccionado = equipo
                                equiposExpanded = false
                            },
                            text = { Text(equipo) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = equipoCosto,
                onValueChange = { equipoCosto = it.filter { char -> char.isDigit() } },
                label = { Text("Costo unitario") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (equipoSeleccionado.isNotBlank() && equipoCosto.isNotBlank()) {
                        equipos.add(equipoSeleccionado to "$$equipoCosto")
                        equipoSeleccionado = ""
                        equipoCosto = ""
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Agregar equipo")
            }

            equipos.forEach { (nombre, costo) ->
                Text("$nombre: $costo", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Segunda visita
            Text("¿Se requiere una segunda visita?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            var segundaVisitaExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = segundaVisitaExpanded,
                onExpandedChange = { segundaVisitaExpanded = !segundaVisitaExpanded }
            ) {
                OutlinedTextField(
                    value = segundaVisita,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione opción") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = segundaVisitaExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = segundaVisitaExpanded,
                    onDismissRequest = { segundaVisitaExpanded = false }
                ) {
                    opcionesSegundaVisita.forEach { opcion ->
                        DropdownMenuItem(
                            onClick = {
                                segundaVisita = opcion
                                segundaVisitaExpanded = false
                            },
                            text = { Text(opcion) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para guardar
            Button(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Información guardada por $technicianName el $formattedDateTime",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Guardar")
            }
        }
    }
}


@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient): LatLng? {
    return try {
        val location = fusedLocationClient.lastLocation.await()
        location?.let {
            LatLng(it.latitude, it.longitude)
        }
    } catch (e: Exception) {
        null
    }
}
