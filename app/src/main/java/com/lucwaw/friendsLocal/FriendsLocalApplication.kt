package com.lucwaw.friendsLocal

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FriendsLocalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.MAPS_API_KEY)
    }
}