package com.lucwaw.friendsLocal.ui.update

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.places.widget.PlaceAutocomplete
import com.google.android.libraries.places.widget.PlaceAutocompleteActivity
import com.lucwaw.friendsLocal.R
import com.lucwaw.friendsLocal.domain.model.Person

@Composable
fun UpdateScreen(
    idPerson: Long,
    viewModel: UpdateViewModel = hiltViewModel<UpdateViewModel>(),
    back: () -> Unit
) {
    val person by viewModel.person.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadPerson(idPerson)
    }

    Scaffold(
        topBar =
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.update_person),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(
                        onClick = { viewModel.onEvent(UpdateEvent.OnDeleteClick);back() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            }
    ) { innerPadding ->
        UpdateContent(Modifier.padding(innerPadding), person, viewModel::onEvent, back)
    }
}

@Composable
fun UpdateContent(
    modifier: Modifier = Modifier,
    person: Person,
    event: (UpdateEvent) -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val startAutocomplete =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = PlaceAutocomplete.getPredictionFromIntent(intent)
                    if (place != null) {
                        event(
                            UpdateEvent.OnAddressChange(
                                place.getFullText(StyleSpan(Typeface.NORMAL)).toString()
                            )
                        )
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
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .border(3.dp, MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = person.firstName,
                onValueChange = { event(UpdateEvent.OnFirstNameChange(it)) },
                label = { Text(stringResource(R.string.first_name)) }
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = person.lastName,
                onValueChange = { event(UpdateEvent.OnLastNameChange(it)) },
                label = { Text(stringResource(R.string.last_name)) }
            )
        }
        OutlinedTextField(
            value = person.address ?: "",
            onValueChange = { event(UpdateEvent.OnAddressChange(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.address)) },
            trailingIcon = {
                IconButton(onClick = {
                    val intent = PlaceAutocomplete.createIntent(context)
                    startAutocomplete.launch(intent)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_for_an_address)
                    )
                }
            }
        )


        Button(
            onClick = { event(UpdateEvent.OnSaveClick(context)); back() }
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
