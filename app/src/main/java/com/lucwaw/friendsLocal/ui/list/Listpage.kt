package com.lucwaw.friendsLocal.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.lucwaw.friendsLocal.R
import com.lucwaw.friendsLocal.domain.model.Person

@Composable
fun ListPage(
    onPersonLocation: (LatLng) -> Unit,
    onUpdatePerson: (Long) -> Unit,
    onAdd: () -> Unit,
    viewModel: ListViewModel = hiltViewModel<ListViewModel>()
) {
    val persons = viewModel.state.value.persons

    var showAlertNoLocationDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton =
            {
                FloatingActionButton(
                    onClick = { onAdd() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Person"
                    )
                }
            }
    ) { paddingValues ->

        LazyColumn(Modifier.padding(paddingValues)) {
            items(
                items = persons,
                key = { person ->
                    person.id
                }
            ) { person ->
                PersonItem(
                    person,
                    {
                        if (person.lat != null && person.lng != null) {
                            onPersonLocation(LatLng(person.lat, person.lng))
                        } else {
                            showAlertNoLocationDialog = true
                        }
                    },
                    { onUpdatePerson(person.id) })
            }
        }
        if (showAlertNoLocationDialog) {
            Dialog(
                onDismissRequest = { showAlertNoLocationDialog = false }
            ) {
                Card {// TODO voir SI necessaire
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(R.string.this_person_does_not_have_a_location_set))
                        TextButton(
                            onClick = { showAlertNoLocationDialog = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonItem(person: Person, onPersonLocation: () -> Unit, goToUpdatePerson: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${person.firstName} ${person.lastName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${person.address} ${person.lat}, ${person.lng}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row {
            IconButton(
                onClick = onPersonLocation
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Arrow"
                )
            }
            IconButton(
                onClick = goToUpdatePerson
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Arrow"
                )
            }
        }

    }
}

@Preview
@Composable
fun PersonItemPreview() {
    PersonItem(
        person = Person(
            lat = 48.8566,
            lng = 2.3522,
            id = 1,
            firstName = "Jean",
            lastName = "Dupont"
        ),
        onPersonLocation = {}
    )
}