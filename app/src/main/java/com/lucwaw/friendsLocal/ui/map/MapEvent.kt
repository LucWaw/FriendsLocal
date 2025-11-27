package com.lucwaw.friendsLocal.ui.map

import android.content.Context
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    data class OnMapLongClick(val latLng: LatLng,val context: Context): MapEvent()
}