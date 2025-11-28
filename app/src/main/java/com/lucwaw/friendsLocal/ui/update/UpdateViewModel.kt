package com.lucwaw.friendsLocal.ui.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import com.lucwaw.friendsLocal.ui.AutoComplete.getLatitudeAndLongitudeFromAddressName
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
    private var originalAddress: String? = null

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
                originalAddress = person?.address
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

                    val hasAddressChanged = newAddress != originalAddress

                    val latLong =
                        if (hasAddressChanged && !newAddress.isNullOrBlank()) {
                            getLatitudeAndLongitudeFromAddressName(
                                event.context,
                                newAddress
                            )
                        } else {
                            current.lat to current.lng
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
                _person.update { it.copy(lat = event.latitude.toDoubleOrNull()) }
            }
            is UpdateEvent.OnLongChange -> {
                _person.update { it.copy(lat = event.longitude.toDoubleOrNull()) }
            }
        }
    }
}