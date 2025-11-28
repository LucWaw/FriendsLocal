package com.lucwaw.friendsLocal.ui.add

import android.app.Activity.RESULT_OK
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.PlaceAutocomplete
import com.google.android.libraries.places.widget.PlaceAutocompleteActivity
import com.lucwaw.friendsLocal.R
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.ui.AutoComplete.getLatitudeAndLongitudeFromAddressName
import com.lucwaw.friendsLocal.ui.update.UpdateContent


@Composable
fun AddPersonPage(
    position: LatLng?,
    viewModel: AddPersonViewModel = hiltViewModel(),
    back: () -> Unit,
) {

    val context = LocalContext.current
    val addressFromViewModel by viewModel.address.collectAsStateWithLifecycle()

    var currentAddress by remember { mutableStateOf(addressFromViewModel) }

    LaunchedEffect(addressFromViewModel) {
        currentAddress = addressFromViewModel
    }

    LaunchedEffect(position) {
        if (position != null) {
            viewModel.loadAddress(context, position.latitude, position.longitude)
        }
    }

    AddPersonContent(
        address = currentAddress,
        latitude = position?.latitude,
        longitude = position?.longitude,
        back = back,
        onAddressChange = { newAddress ->
            currentAddress = newAddress // On met à jour l'état local quand l'utilisateur tape
        },
        event = viewModel::onEvent,
    )
}

@Composable
fun AddPersonContent(
    modifier: Modifier = Modifier,
    address: String,
    latitude: Double?,
    longitude: Double?,
    back: () -> Unit,
    event: (AddEvent) -> Unit,
    onAddressChange: (String) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(latitude.toString()) }
    var longitude by remember { mutableStateOf(longitude.toString()) }
    val onLatLngChange = { newLat: Double?, newLng: Double? ->
        latitude = newLat.toString()
        longitude = newLng.toString()
    }
    val context = LocalContext.current


    val startAutocomplete =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val result = result.data
                if (result != null) {
                    val place = PlaceAutocomplete.getPredictionFromIntent(result)
                    if (place != null) {
                        onAddressChange(place.getFullText(StyleSpan(Typeface.NORMAL)).toString())
                        val result = getLatitudeAndLongitudeFromAddressName(
                            context,
                            place.getFullText(StyleSpan(Typeface.NORMAL)).toString()
                        )
                        onLatLngChange(result?.first, result?.second)
                    }
                }
            } else if (result.resultCode == PlaceAutocompleteActivity.RESULT_ERROR) {
                val intent = result.data
                if (intent != null) {
                    val status = PlaceAutocomplete.getResultStatusFromIntent(intent)
                    Toast.makeText(
                        context,
                        "Failed to get place '${status?.statusMessage}'",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier
                .border(3.dp, MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.first_name)) }
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.last_name)) }
            )
        }

        OutlinedTextField(
            value = address,
            onValueChange = { onAddressChange(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.address)) },
            trailingIcon = {
                IconButton(onClick = {
                    val intent =
                        PlaceAutocomplete.IntentBuilder().setInitialQuery(address).build(context)
                    startAutocomplete.launch(intent)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_for_an_address)
                    )
                }
            }
        )

        LatLngEditor(
            address = address,
            lat = latitude,
            lng = longitude,
            onLatLngChange = onLatLngChange
        )

        Button(
            onClick = {
                val latLong = getLatitudeAndLongitudeFromAddressName(context, address)
                event(
                    AddEvent.OnCreate(
                        Person(
                            firstName = firstName,
                            lastName = lastName,
                            address = address,
                            lat = latLong?.first,
                            lng = latLong?.second
                        )
                    )
                ); back()
            }
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun LatLngEditor(
    address: String,
    lat: String,
    lng: String,
    onLatLngChange: (Double?, Double?) -> Unit
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.coordinates),
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = if (lat == "null") "" else lat,
                onValueChange = { text ->
                    onLatLngChange(text.toDoubleOrNull(), lng.toDoubleOrNull())
                },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.latitude)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = if (lng == "null") "" else lng,
                onValueChange = { text ->
                    onLatLngChange(lat.toDoubleOrNull(), text.toDoubleOrNull())
                },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.longitude)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            IconButton(
                onClick = {
                    val result = getLatitudeAndLongitudeFromAddressName(context, address)
                    onLatLngChange(result?.first, result?.second)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh_coordinates)
                )
            }
        }
    }
}


@Preview(locale = "fr")
@Composable
fun UpdateContentPreview() {
    UpdateContent(
        modifier = Modifier.height(700.dp),
        person = Person(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            address = "123 Main St"
        ),
        event = {},
        back = {}
    )
}