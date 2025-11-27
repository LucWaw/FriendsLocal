package com.lucwaw.friendsLocal.domain.model

import android.location.Address

data class Person (
    val lat: Double? = null,
    val lng: Double? = null,
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val address: String? = null,
)