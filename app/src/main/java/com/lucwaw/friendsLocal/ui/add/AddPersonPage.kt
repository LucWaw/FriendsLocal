package com.lucwaw.friendsLocal.ui.add

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.lucwaw.friendsLocal.R
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.ui.update.UpdateContent

@Composable
fun AddPersonPage(
    position: LatLng?,
    viewModel: AddPersonViewModel = hiltViewModel(),
    back: () -> Unit,
) {
    val context = LocalContext.current
    val address by viewModel.address.collectAsStateWithLifecycle()

    LaunchedEffect(position) {
        Log.d(
            "LATTTT LONNG",
            "${position?.latitude ?: "aa"} ${position?.longitude ?: "bb"} "
        )
        if (position != null) {
            viewModel.loadAddress(context, position.latitude, position.longitude)
        }
    }

    AddPersonContent(
        address = address,
        back = back,
        event = viewModel::onEvent
    )
}

@Composable
fun AddPersonContent(
    modifier: Modifier = Modifier,
    address: String,
    back: () -> Unit,
    event: (AddEvent) -> Unit,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = { event(AddEvent.OnUpdateAddress(it)) },
            label = { Text(stringResource(R.string.address)) }
        )
        Button(
            onClick = {
                event(
                    AddEvent.OnCreate(
                        Person(
                            firstName = firstName,
                            lastName = lastName,
                            address = address,
                        )
                    )
                ); back()
            }
        ) {
            Text(stringResource(R.string.save))
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