package com.example.rcm.views.acta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import com.example.rcm.R
import com.example.rcm.views.navbar.NavBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ActaView(navController: NavController) {
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
                        .padding(horizontal = 16.dp)
                ) {
                    // Próximas actas
                    SectionWithScrollableItems(
                        title = "Próximas actas",
                        items = listOf(
                            "Terpel Aeropuerto_02-12-24",
                            "Terpel Pontevedra_02-12-24",
                            "Meals de Colombia_03-12-24",
                            "Mazda Morato_04-12-24",
                            "Shell 116_05-12-24",
                            "Empresa ABC_06-12-24"
                        ),
                        navController = navController,
                        isFuture = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Actas pasadas
                    SectionWithScrollableItems(
                        title = "Actas pasadas",
                        items = listOf(
                            "Terpel Aeropuerto_25-11-24",
                            "Terpel Serviruedas_25-11-24",
                            "Caracol_22-11-24",
                            "Toyota Morato_21-11-24",
                            "Empresa XYZ_20-11-24",
                            "Uniandes_18-11-24"
                        ),
                        navController = navController,
                        isFuture = false
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

@Composable
fun SectionWithScrollableItems(
    title: String,
    items: List<String>,
    navController: NavController,
    isFuture: Boolean
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // Caja que contiene el scroll y los íconos
    Box {
        val scrollState = rememberScrollState()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // Limitar la altura de la tarjeta
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState) // Habilitar scroll
                    .padding(16.dp)
            ) {
                items.forEachIndexed { index, item ->
                    // Dividir título y fecha
                    val splitData = item.split("_")
                    val titleText = splitData.getOrNull(0) ?: "Título desconocido"
                    val dateText = splitData.getOrNull(1) ?: "Fecha desconocida"
                    val formattedDate = dateText.replace("-", "/")

                    ActaListItem(
                        title = titleText,
                        date = formattedDate,
                        id = "$index",
                        navController = navController,
                        isFuture = isFuture
                    )
                    if (index != items.size - 1) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }

        // Icono para deslizar hacia arriba
        if (scrollState.value > 0) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_up_24),
                contentDescription = "Scroll up",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .size(24.dp),
                tint = Color.Gray
            )
        }

        // Icono para deslizar hacia abajo
        if (scrollState.value < scrollState.maxValue) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_down_24),
                contentDescription = "Scroll down",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .size(24.dp),
                tint = Color.Gray
            )
        }
    }
}


@Composable
fun ActaListItem(
    title: String,
    date: String,
    id: String,
    navController: NavController,
    isFuture: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(
            onClick = {
                if (isFuture) {
                    navController.navigate("acta_futura/${title.replace(" ", "_")}")
                } else {
                    navController.navigate("detalle_acta/${title.replace(" ", "_")}")
                }
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_forward_24),
                contentDescription = "Abrir Acta",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
