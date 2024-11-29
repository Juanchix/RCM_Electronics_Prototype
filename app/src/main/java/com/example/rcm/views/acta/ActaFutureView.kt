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
import androidx.core.location.LocationManagerCompat.getCurrentLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActaFutureView(
    navController: NavController,
    context: Context,
    actaTitle: String
) {

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
    var segundaVisita by remember { mutableStateOf("") }

    val opcionesSegundaVisita = listOf("Sí", "No")

    val ubicacionRCM = LatLng(4.694504593530979, -74.07515704525008)

    // Información de categorías, marcas y dispositivos
    val equiposData = mapOf(
        "Control de Acceso" to mapOf(
            "Rosslare" to listOf(
                "Controlador AC-215",
                "Controlador AC-425",
                "Teclado AY-K12",
                "Lector AY-L23",
                "Cerradura EL-300"
            ),
            "ZKTeco" to listOf(
                "Controlador ZK-700",
                "Controlador ZK-900",
                "Teclado ProCapture-X",
                "Lector InBio-460",
                "Panel MA300"
            ),
            "D-Tech" to listOf(
                "Controlador DT-100",
                "Controlador DT-200",
                "Lector DT-LX100",
                "Teclado DT-KPAD",
                "Cerradura DT-SMARTLOCK"
            )
        ),
        "CCTV" to mapOf(
            "D-Tech" to listOf(
                "Cámara DT-500",
                "Cámara DT-700",
                "DVR DT-X500",
                "NVR DT-N300",
                "Cámara IP DT-IP900"
            ),
            "Hikvision" to listOf(
                "Cámara DS-2CD",
                "Cámara DS-2DE",
                "DVR DS-7200",
                "NVR DS-7600",
                "Cámara IP DS-2CV"
            )
        )
    )

    var categoriaSeleccionada by remember { mutableStateOf("") }
    var marcaSeleccionada by remember { mutableStateOf("") }
    var dispositivoSeleccionado by remember { mutableStateOf("") }
    var equipos by remember { mutableStateOf(mutableListOf<Pair<String, String>>()) }
    var equipoCosto by remember { mutableStateOf("") }


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
                    text = "$formattedActaTitle",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
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

            // Mapa con ubicación fija
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
                        ubicacionRCM, 15f
                    )
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(state = MarkerState(position = ubicacionRCM), title = "Sede RCM")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de visita
            Text("Tipo de Visita:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            var expanded by remember { mutableStateOf(false) }
            val opciones =
                listOf("Cotización", "Instalación", "Mantenimiento", "Reparación", "Control")

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
                    onValueChange = {
                        horasDuracion = it.filter { char -> char.isDigit() }.take(2)
                    },
                    label = { Text("Horas") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = minutosDuracion,
                    onValueChange = {
                        minutosDuracion = it.filter { char -> char.isDigit() }.take(2)
                    },
                    label = { Text("Minutos") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text("Equipos Implicados:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            // Selección de categoría
            Text("Categoría de equipo:")
            var categoriaExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoriaExpanded,
                onExpandedChange = { categoriaExpanded = !categoriaExpanded }
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione categoría") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = categoriaExpanded,
                    onDismissRequest = { categoriaExpanded = false }
                ) {
                    equiposData.keys.forEach { categoria ->
                        DropdownMenuItem(
                            onClick = {
                                categoriaSeleccionada = categoria
                                marcaSeleccionada = ""
                                dispositivoSeleccionado = ""
                                categoriaExpanded = false
                            },
                            text = { Text(categoria) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selección de marca
            if (categoriaSeleccionada.isNotEmpty()) {
                Text("Marca de equipo:")
                var marcaExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = marcaExpanded,
                    onExpandedChange = { marcaExpanded = !marcaExpanded }
                ) {
                    OutlinedTextField(
                        value = marcaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccione marca") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = marcaExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = marcaExpanded,
                        onDismissRequest = { marcaExpanded = false }
                    ) {
                        equiposData[categoriaSeleccionada]?.keys?.forEach { marca ->
                            DropdownMenuItem(
                                onClick = {
                                    marcaSeleccionada = marca
                                    dispositivoSeleccionado = ""
                                    marcaExpanded = false
                                },
                                text = { Text(marca) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Selección de dispositivo
            if (marcaSeleccionada.isNotEmpty()) {
                Text("Dispositivo:")
                var dispositivoExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = dispositivoExpanded,
                    onExpandedChange = { dispositivoExpanded = !dispositivoExpanded }
                ) {
                    OutlinedTextField(
                        value = dispositivoSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccione dispositivo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dispositivoExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = dispositivoExpanded,
                        onDismissRequest = { dispositivoExpanded = false }
                    ) {
                        equiposData[categoriaSeleccionada]?.get(marcaSeleccionada)
                            ?.forEach { dispositivo ->
                                DropdownMenuItem(
                                    onClick = {
                                        dispositivoSeleccionado = dispositivo
                                        dispositivoExpanded = false
                                    },
                                    text = { Text(dispositivo) }
                                )
                            }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Campo para costo del equipo
            if (dispositivoSeleccionado.isNotEmpty()) {
                OutlinedTextField(
                    value = equipoCosto,
                    onValueChange = { equipoCosto = it.filter { char -> char.isDigit() } },
                    label = { Text("Costo unitario") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (dispositivoSeleccionado.isNotBlank() && equipoCosto.isNotBlank()) {
                            equipos.add(
                                "$dispositivoSeleccionado" to "$$equipoCosto"
                            )
                            dispositivoSeleccionado = ""
                            equipoCosto = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Agregar equipo")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Mostrar equipos agregados
            if (equipos.isNotEmpty()) {
                Text("Equipos agregados:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                equipos.forEach { (nombre, costo) ->
                    Text("$nombre: $costo", fontSize = 16.sp)
                }
            }

                Spacer(modifier = Modifier.height(24.dp))

                // Segunda visita
                Text(
                    "¿Se requiere una segunda visita?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
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

            Spacer(modifier = Modifier.height(24.dp))

                // Detalles de la visita
                Text("Detalles de la visita:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                var detallesVisita by remember { mutableStateOf("") }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = detallesVisita,
                        onValueChange = {
                            if (it.length <= 400) detallesVisita = it
                        },
                        label = { Text("Ingrese detalles de la visita") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        maxLines = 4
                    )
                    // Contador de caracteres
                    Text(
                        text = "${400 - detallesVisita.length} caracteres restantes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                    )
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