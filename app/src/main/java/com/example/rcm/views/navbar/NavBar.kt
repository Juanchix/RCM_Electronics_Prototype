package com.example.rcm.views.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rcm.R

@Composable
fun NavBar(navController: NavController) {
    val isLightTheme = !isSystemInDarkTheme()

    val lightGrayNavBar = Color(0xFFDDDDDD)
    val darkGrayNavBar = Color(0xFF2D2D2D)
    val backgroundColor = if (isLightTheme) lightGrayNavBar else darkGrayNavBar
    val iconColor = if (isLightTheme) Color.Black else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(77.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Home",
                iconRes = R.drawable.home_24,
                isSelected = navController.isCurrentRoute("home"),
                navController = navController,
                route = "home",
                iconColor = iconColor
            )
            NavItem(
                label = "Actas",
                iconRes = R.drawable.article_24,
                isSelected = navController.isCurrentRoute("actas"),
                navController = navController,
                route = "actas",
                iconColor = iconColor
            )
            NavItem(
                label = "Perfil",
                iconRes = R.drawable.person_24,
                isSelected = navController.isCurrentRoute("profile"), // Asegúrate de usar "profile"
                navController = navController,
                route = "profile", // Cambiado de "perfil" a "profile"
                iconColor = iconColor
            )
        }
    }
}


@Composable
fun NavItem(label: String, iconRes: Int, isSelected: Boolean, navController: NavController, route: String, iconColor: Color) {
    val color = if (isSelected) Color(0xFF5875DD) else iconColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            if (!isSelected) navController.navigate(route)
        }) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = color,
            fontSize = 12.sp
        )
    }
}

@Composable
fun NavController.isCurrentRoute(route: String): Boolean {
    return this.currentBackStackEntryAsState().value?.destination?.route == route
}
