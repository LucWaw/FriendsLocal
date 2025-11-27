package com.lucwaw.friendsLocal.ui.map

import com.google.android.gms.maps.model.LatLng
import com.lucwaw.friendsLocal.domain.model.Person

sealed class MapEvent {
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
}