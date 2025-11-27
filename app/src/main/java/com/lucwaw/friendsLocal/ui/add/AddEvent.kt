package com.lucwaw.friendsLocal.ui.add

import com.lucwaw.friendsLocal.domain.model.Person

sealed class AddEvent {
    data class OnCreate(val person: Person) : AddEvent()
    data class OnUpdateAddress(val address: String) : AddEvent()
}