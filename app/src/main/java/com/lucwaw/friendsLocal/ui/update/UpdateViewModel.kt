package com.lucwaw.friendsLocal.ui.update

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.friendsLocal.domain.model.Person
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import com.lucwaw.friendsLocal.ui.map.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    private val _person = MutableStateFlow(Person(
        id = 0,
        firstName = "",
        lastName = "",
    ))
    val person = _person.asStateFlow()

    fun loadPerson(id: Int) {
        viewModelScope.launch {
            _person.update {
                repository.getPersonById(id) ?: it
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
            is UpdateEvent.OnPlaceSelected -> {
                //TODO GEOCODE and update lat lng in person
            }
            UpdateEvent.OnSaveClick -> {
                viewModelScope.launch {
                    repository.insertPerson(person.value)
                }
            }
        }
    }
}