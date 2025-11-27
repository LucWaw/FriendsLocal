package com.lucwaw.friendsLocal.domain.model

data class Person (
    val lat: Double,
    val lng: Double,
    val id: Long = 0,
    val firstName: String,
    val lastName: String
)