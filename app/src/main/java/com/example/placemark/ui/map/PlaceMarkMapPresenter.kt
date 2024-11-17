package com.example.placemark.ui.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.placemark.models.PlaceMarkModel
import com.example.placemark.ui.BasePresenter
import com.example.placemark.ui.BaseViewActivity

class PlaceMarkMapPresenter(view: BaseViewActivity) : BasePresenter(view) {

    fun doConfigureMap(map: GoogleMap, listener: GoogleMap.OnMarkerClickListener){
        map.uiSettings.isZoomControlsEnabled = true
        app.placemarks.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
            map.setOnMarkerClickListener(listener)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun getPlaceMark(marker: Marker) : PlaceMarkModel {
        val placemark = app.placemarks.findById(marker.tag as Long)
        return placemark
    }

}