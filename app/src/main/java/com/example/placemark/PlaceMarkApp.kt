package com.example.placemark

import android.app.Application
import com.example.placemark.models.PlaceMarkJSONStore
import com.example.placemark.models.PlaceMarkStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlaceMarkApp : Application(){
    lateinit var placemarks: PlaceMarkStore

    override fun onCreate() {
        super.onCreate()
        placemarks = PlaceMarkJSONStore(applicationContext)
    }
}