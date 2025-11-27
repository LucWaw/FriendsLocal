package com.lucwaw.friendsLocal.ui.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    private val _state = mutableStateOf(MapState())
    val state: State<MapState> = _state


    init {
        viewModelScope.launch {
            repository.getPersons().collectLatest { spots ->
                _state.value = _state.value.copy(
                    persons = spots
                )
            }
        }
    }

    fun updateLastUpdatedAt(value: Int?) {
        lastUpdatedAt.value = value
    }

    var lastUpdatedAt = mutableStateOf<Int?>(null)

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    lastUpdatedAt.value = repository.insertPerson(
                        Person(
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude,
                            firstName = "none",
                            lastName = "none",
                            address = reverseGeocode(
                                event.context,
                                event.latLng.latitude,
                                event.latLng.longitude
                            )?.toShortAddress()
                        )
                    )
                }
            }
        }
    }

    suspend fun reverseGeocode(
        context: Context,
        lat: Double,
        lng: Double
    ): Address? = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // API 33+
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        val first = addresses.firstOrNull()
                        continuation.resume(first) { cause, _, _ -> }
                    }

                    override fun onError(errorMessage: String?) {
                        Log.e("Geocode", "Error during geocoding: $errorMessage")

                        continuation.resume(null) { cause, _, _ ->
                        }
                    }
                })
            }
        } else {
            // API 21–32 (ancienne API sync)
            try {
                geocoder.getFromLocation(lat, lng, 1)?.firstOrNull()
            } catch (e: Exception) {
                Log.e("Geocode old", "Error during geocoding: ${e.message}")
                null
            }
        }
    }

    fun Address.toShortAddress(): String {
        val streetNumber = subThoroughfare   // ex: "39"
        val streetName = thoroughfare        // ex: "Boulevard du Temple"
        val postal = postalCode              // ex: "75003"
        val city = locality ?: subAdminArea  // ex: "Paris"

        val street = listOfNotNull(streetNumber, streetName)
            .joinToString(" ")

        val cityPart = listOfNotNull(postal, city)
            .joinToString(" ")

        return if (street != "") {
            listOfNotNull(street, cityPart)
                .joinToString(", ")
                .uppercase()
        } else {
            cityPart.uppercase()
        }
    }
}