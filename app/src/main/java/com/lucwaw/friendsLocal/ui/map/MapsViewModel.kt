package com.lucwaw.friendsLocal.ui.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    repository.insertPerson(
                        Person(
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude,
                            firstName = "none",
                            lastName = "none"
                        )
                    )
                }
            }
        }
    }
}