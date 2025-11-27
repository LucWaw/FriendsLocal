package com.lucwaw.friendsLocal.ui.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.friendsLocal.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
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
}