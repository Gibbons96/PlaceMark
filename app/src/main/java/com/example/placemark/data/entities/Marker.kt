package com.example.placemark.data.entities

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Marker(
    val title: String = "",
    val subtitle: String = "",
    val image: Uri? = null,
    val markerLat: String,
    val markerLong: String,
)
