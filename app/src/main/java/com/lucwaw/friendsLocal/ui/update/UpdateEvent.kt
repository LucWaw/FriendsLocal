package com.lucwaw.friendsLocal.ui.update

import com.google.android.gms.maps.model.LatLng

sealed class UpdateEvent {
    data class OnFirstNameChange(val firstName: String) : UpdateEvent()
    data class OnLastNameChange(val lastName: String) : UpdateEvent()
    data class OnAddressChange(val address: String) : UpdateEvent()
    data object OnSaveClick : UpdateEvent()
    data class OnPlaceSelected(val latLng: LatLng, val address: String) : UpdateEvent()
    data object OnDeleteClick : UpdateEvent()
}