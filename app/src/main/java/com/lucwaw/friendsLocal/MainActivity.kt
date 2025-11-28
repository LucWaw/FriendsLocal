package com.lucwaw.friendsLocal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.LatLng
import com.lucwaw.friendsLocal.ui.BottomBar
import com.lucwaw.friendsLocal.ui.add.AddPersonPage
import com.lucwaw.friendsLocal.ui.list.ListPage
import com.lucwaw.friendsLocal.ui.map.MapScreen
import com.lucwaw.friendsLocal.ui.theme.FriendsLocalTheme
import com.lucwaw.friendsLocal.ui.update.UpdateScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FriendsLocalTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    }) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "ListPage",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable("ListPage") {
                            ListPage(onUpdatePerson = { id ->
                                navController.navigate("Update/$id")
                            }, onAdd = {
                                navController.navigate("Add/")
                            }, onPersonLocation = { latlng: LatLng ->
                                navController.navigate("Map?lat=${latlng.latitude}&lng=${latlng.longitude}")
                            })
                        }

                        composable(
                            route = "Map?lat={lat}&lng={lng}",
                            arguments = listOf(navArgument("lat") {
                                type = NavType.FloatType
                                defaultValue = -100f
                            }, navArgument("lng") {
                                type = NavType.FloatType
                                defaultValue = -100f
                            })
                        ) { backStackEntry ->
                            val lat = backStackEntry.arguments?.getFloat("lat")
                            val lng = backStackEntry.arguments?.getFloat("lng")

                            var startPosition: LatLng? = null
                            if (lat != null && lng != null && lat != -100f && lng != -100f) {
                                startPosition = LatLng(lat.toDouble(), lng.toDouble())
                            }

                            MapScreen(
                                startPosition = startPosition,
                                onUpdatePerson = { id ->
                                    navController.navigate("Update/$id")
                                },
                                onAdd = { latlng ->

                                    navController.navigate("Add/?lat=${latlng.latitude}&lng=${latlng.longitude}")
                                })
                        }

                        composable(
                            route = "Add/?lat={lat}&lng={lng}",
                            arguments = listOf(navArgument("lat") {
                                type = NavType.FloatType
                                defaultValue = -100f
                            }, navArgument("lng") {
                                type = NavType.FloatType
                                defaultValue = -100f
                            })
                        ) { backStackEntry ->
                            val lat = backStackEntry.arguments?.getFloat("lat")
                            val lng = backStackEntry.arguments?.getFloat("lng")

                            var position: LatLng? = null
                            if (lat != null && lng != null && lat != -100.0f && lng != -100.0f) {
                                position = LatLng(lat.toDouble(), lng.toDouble())
                            }

                            AddPersonPage(position = position) {
                                navController.navigate("ListPage")
                            }
                        }

                        composable(
                            route = "Update/{personId}", arguments = listOf(
                                navArgument("personId") {
                                    type = NavType.LongType
                                })
                        ) { backStackEntry ->

                            val personId = backStackEntry.arguments?.getLong("personId")

                            if (personId != null) {
                                UpdateScreen(personId, back = {
                                    navController.navigate("ListPage")
                                })
                            } else {
                                Log.e("MainActivity", "personId is null in UpdateScreen navigation")
                                Text(
                                    text = "Error: Person ID is missing."
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}