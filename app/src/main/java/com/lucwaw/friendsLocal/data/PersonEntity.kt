package com.lucwaw.friendsLocal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firstName: String,

    val lastName: String,

    //nullable values for location details versatility
    val lat: Double? = null,

    val lng: Double? = null,

    val address: String? = null,
)
