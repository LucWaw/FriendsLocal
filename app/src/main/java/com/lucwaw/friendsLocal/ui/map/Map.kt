package com.lucwaw.friendsLocal.ui.map

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.maps.model.AdvancedMarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

private val WORLD_POSITION = LatLng(20.0, 0.0)
private const val WORLD_ZOOM = 1f // Le niveau de zoom le plus faible
private const val DETAIL_ZOOM = 15f

@Composable
fun MapScreen(
    startPosition: LatLng? = null, viewModel: MapsViewModel = hiltViewModel<MapsViewModel>(),
    onUpdatePerson: (Int) -> Unit
) {
    if (viewModel.lastUpdatedAt.value != null){
        onUpdatePerson(viewModel.lastUpdatedAt.value!!)
        viewModel.updateLastUpdatedAt(null)
    }

    val initialCameraPosition = if (startPosition != null) {
        CameraPosition.fromLatLngZoom(startPosition, DETAIL_ZOOM)
    } else {
        CameraPosition.fromLatLngZoom(WORLD_POSITION, WORLD_ZOOM)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = initialCameraPosition
    }

    val state by viewModel.state

    val uiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        compassEnabled = true
    )

    Scaffold { paddingValues ->
        val context = LocalContext.current

        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.padding(paddingValues),
            properties = state.properties,
            uiSettings = uiSettings,
            onMapLongClick = { latLng ->
                viewModel.onEvent(MapEvent.OnMapLongClick(latLng, context))
            },
        ) {
            state.persons.forEachIndexed { index, person ->
                if (person.lat == null || person.lng == null) {
                    return@forEachIndexed
                }
                val markerState = rememberUpdatedMarkerState(
                    position = LatLng(person.lat, person.lng)
                )
                AdvancedMarker(
                    state = markerState,
                    title = "${person.firstName} ${person.lastName}",
                    snippet = person.address ?: "(${person.lat}, ${person.lng})",
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    onInfoWindowClick = {
                        onUpdatePerson(person.id)
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            }
        }
    }
}