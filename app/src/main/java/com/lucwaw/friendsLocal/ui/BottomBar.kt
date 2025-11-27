package com.lucwaw.friendsLocal.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: androidx.navigation.NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List") },
            label = { Text("Liste") },
            selected = currentRoute == "ListPage",
            onClick = {
                navController.navigate("ListPage") {
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Map, contentDescription = "Carte") },
            label = { Text("Carte") },
            selected = currentRoute == "Map",
            onClick = {
                navController.navigate("Map") {
                    launchSingleTop = true
                }
            }
        )
    }
}
