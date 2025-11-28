package com.lucwaw.friendsLocal.domain.model

data class Person(
    val lat: Double? = null,
    val lng: Double? = null,
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val address: String? = null,
)