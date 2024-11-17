package com.example.placemark.ui.map

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.example.placemark.R
import kotlinx.android.synthetic.main.activity_placemark_maps.*
import kotlinx.android.synthetic.main.content_placemark_maps.*
import com.example.placemark.helpers.readImageFromPath
import com.example.placemark.ui.BaseViewActivity


class PlaceMarkMapsViewActivity : BaseViewActivity(), GoogleMap.OnMarkerClickListener {

  lateinit var map: GoogleMap
  lateinit var presenter: PlaceMarkMapPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_maps)
    setSupportActionBar(toolbarMaps)
    presenter = PlaceMarkMapPresenter(this)

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync{
      map = it
      configureMap()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }

  fun configureMap() {
    presenter.doConfigureMap(map, this)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    val placemark = presenter.getPlaceMark(marker)
    currentTitle.text = placemark.title
    currentDescription.text = placemark.description
    placemarkImageView.setImageBitmap(readImageFromPath(this, placemark.image))
    return true
  }

}