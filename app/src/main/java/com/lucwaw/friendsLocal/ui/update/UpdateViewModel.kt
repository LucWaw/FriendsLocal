package com.lucwaw.friendsLocal.ui.update

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import com.lucwaw.friendsLocal.ui.autocomplete.getLatitudeAndLongitudeFromAddressName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {
    private val _latitude = mutableStateOf("")
    val latitude: State<String> = _latitude

    private val _longitude = mutableStateOf("")
    val longitude: State<String> = _longitude

    private val _person = MutableStateFlow(
        Person(
            id = 0,
            firstName = "",
            lastName = "",
        )
    )
    val person = _person.asStateFlow()

    fun loadPerson(id: Long) {
        viewModelScope.launch {
            _person.update {
                val person = repository.getPersonById(id)
                _latitude.value = person?.lat.toString()
                _longitude.value = person?.lng.toString()
                person ?: it
            }
        }
    }


    fun onEvent(event: UpdateEvent) {
        when (event) {
            is UpdateEvent.OnAddressChange -> {
                _person.update { it.copy(address = event.address) }
            }

            UpdateEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    repository.deletePerson(person.value)
                }
            }

            is UpdateEvent.OnFirstNameChange -> {
                _person.update { it.copy(firstName = event.firstName) }
            }

            is UpdateEvent.OnLastNameChange -> {
                _person.update { it.copy(lastName = event.lastName) }
            }

            is UpdateEvent.OnSaveClick -> {
                viewModelScope.launch {

                    val current = person.value
                    val newAddress = current.address


                    val latLong =
                        if (!newAddress.isNullOrBlank() && latitude.value.isEmpty() && longitude.value.isEmpty()) {
                            getLatitudeAndLongitudeFromAddressName(
                                event.context,
                                newAddress
                            )
                        } else {
                            latitude.value.toDoubleOrNull() to longitude.value.toDoubleOrNull()
                        }

                    repository.insertPerson(
                        current.copy(
                            lat = latLong?.first,
                            lng = latLong?.second
                        )
                    )
                }
            }

            is UpdateEvent.OnLatChange -> {
                _latitude.value = event.latitude
            }

            is UpdateEvent.OnLongChange -> {
                _longitude.value = event.longitude

            }
        }
    }
}