package com.lucwaw.friendsLocal.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.ui.map.MapsViewModel

@Composable
fun ListPage(
    onPersonLocation: (LatLng) -> Unit,
    onUpdatePerson: (Long) -> Unit,
    onAdd: () -> Unit,
    viewModel: MapsViewModel = hiltViewModel<MapsViewModel>()
) {
    val persons = viewModel.state.value.persons
    Scaffold { paddingValues ->

        LazyColumn(Modifier.padding(paddingValues)) {
            items(
                items = persons,
                key = { person ->
                    // Return a stable + unique key for the item
                    person.id
                }
            ) { person ->
                PersonItem(
                    person,
                    { onPersonLocation(LatLng(person.lat, person.lng)) },
                    { onUpdatePerson(person.id) })
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "${person.firstName} ${person.lastName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "(${person.lat}, ${person.lng})",
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