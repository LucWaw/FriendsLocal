package com.lucwaw.friendsLocal.ui.update

import android.content.Context

sealed class UpdateEvent {
    data class OnFirstNameChange(val firstName: String) : UpdateEvent()
    data class OnLastNameChange(val lastName: String) : UpdateEvent()
    data class OnAddressChange(val address: String) : UpdateEvent()
    data class OnSaveClick(val context: Context) : UpdateEvent()
    data object OnDeleteClick : UpdateEvent()
}