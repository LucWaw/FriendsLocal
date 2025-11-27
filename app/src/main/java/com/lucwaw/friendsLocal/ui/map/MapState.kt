package com.lucwaw.friendsLocal.ui.map

import com.google.maps.android.compose.MapProperties
import com.lucwaw.friendsLocal.domain.model.Person

data class MapState(
    val properties: MapProperties = MapProperties(),
    val persons: List<Person> = emptyList(),
    val isFalloutMap: Boolean = false
)