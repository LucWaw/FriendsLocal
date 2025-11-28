package com.lucwaw.friendsLocal.ui.AutoComplete

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import java.util.Locale
import java.util.concurrent.CountDownLatch

/**
 * Retrieves the latitude and longitude coordinates for a given address name.
 *
 * This function uses the Geocoder to perform geocoding, which is the process of
 * converting an address string into geographic coordinates (latitude and longitude).
 *
 * @param context The application context. Required for accessing the Geocoder.
 * @param address The address string to geocode (e.g., "1600 Amphitheatre Parkway, Mountain View, CA").
 * @return A Pair containing the latitude and longitude as Doubles, or null if:
 *         - The device is running an Android version below API 33 (Tiramisu). Geocoding only works on API 33 and above on this function.
 *         - The address cannot be found or resolved by the Geocoder.
 *         - An error occurs during the geocoding process.
 *
 * @OptIn(ExperimentalCoroutinesApi::class) Marks this function as using experimental coroutines features.
 */
fun getLatitudeAndLongitudeFromAddressName(
    context: Context,
    address: String
): Pair<Double, Double>? {
    val geocoder = Geocoder(context, Locale.getDefault())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val latch = CountDownLatch(1)
        var result: Pair<Double, Double>? = null

        geocoder.getFromLocationName(
            address, 1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        val firstAddress = addresses[0]
                        result = Pair(firstAddress.latitude, firstAddress.longitude)
                    }
                    latch.countDown() // Libère le thread bloqué
                }

                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    latch.countDown() // Libère le thread bloqué en cas d'erreur
                }
            })

        latch.await() // Bloque le thread principal jusqu'à ce que `countDown()` soit appelé
        return result
    }

    return null
}

