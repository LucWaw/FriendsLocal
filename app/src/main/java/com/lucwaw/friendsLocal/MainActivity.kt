package com.lucwaw.friendsLocal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.LatLng
import com.lucwaw.friendsLocal.ui.AddPersonPage
import com.lucwaw.friendsLocal.ui.BottomBar
import com.lucwaw.friendsLocal.ui.DetailPage
import com.lucwaw.friendsLocal.ui.list.ListPage
import com.lucwaw.friendsLocal.ui.map.MapScreen
import com.lucwaw.friendsLocal.ui.theme.FriendsLocalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FriendsLocalTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "ListPage",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable("ListPage") {
                            ListPage(
                                onUpdatePerson = { id ->
                                    Log.d("DDDD", id.toString())
                                    navController.navigate("Detail/$id")
                                },
                                onAdd = {
                                    navController.navigate("AddPerson")
                                },
                                onPersonLocation = { latlng : LatLng ->
                                    navController.navigate("Map?lat=${latlng.latitude}&lng=${latlng.longitude}")
                                }
                            )
                        }

                        composable(
                            route = "Map?lat={lat}&lng={lng}", // Utilise des query parameters
                            arguments = listOf(
                                // Arguments optionnels avec une valeur par défaut
                                navArgument("lat") {
                                    type = NavType.FloatType
                                    defaultValue = -1f // Une valeur invalide pour savoir si elle a été passée
                                },
                                navArgument("lng") {
                                    type = NavType.FloatType
                                    defaultValue = -1f // Une valeur invalide pour savoir si elle a été passée
                                }
                            )
                        ) { backStackEntry ->
                            // On récupère les arguments
                            val lat = backStackEntry.arguments?.getFloat("lat")
                            val lng = backStackEntry.arguments?.getFloat("lng")

                            var startPosition: LatLng? = null
                            // On vérifie si les arguments ont été passés et ne sont pas la valeur par défaut
                            if (lat != null && lng != null && lat != -1f && lng != -1f) {
                                startPosition = LatLng(lat.toDouble(), lng.toDouble())
                            }

                            // On appelle MapScreen avec la position (ou null si non fournie)
                            MapScreen(startPosition = startPosition)
                        }

                        composable("AddPerson") {
                            AddPersonPage()
                        }

                        composable(
                            route = "Detail/{personId}",
                            arguments = listOf(
                                navArgument("personId") { type = NavType.LongType }
                            )
                        ) { backStackEntry ->

                            val personId = backStackEntry.arguments?.getLong("personId")
                            Log.d("AAAA", personId.toString())

                            DetailPage(personId)
                        }
                    }
                }
            }
        }
    }
}
