package com.lucwaw.friendsLocal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val firstName: String,
    val lastName: String
)