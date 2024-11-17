package com.example.placemark.ui.addPlaceMark

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_add_placemark.*

import com.example.placemark.R
import com.example.placemark.helpers.readImageFromPath
import com.example.placemark.models.PlaceMarkModel
import com.example.placemark.ui.BaseViewActivity
import com.example.placemark.ui.IMAGE_REQUEST

class AddPlaceMarkActivity : BaseViewActivity() {

  private val TAG: String = "AddPlaceMarkActivity"
  lateinit var presenter: AddPlaceMarkPresenter
  var placemark = PlaceMarkModel()
  lateinit var map: GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_placemark)

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      presenter.doConfigureMap(it)
      it.setOnMapClickListener {
        presenter.putTextDataBeforeMove(placemarkTitle.text.toString(),description.text.toString())
        presenter.doSetLocation() }
    }

    val imagePickerLauncher =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null) {
          presenter.doActivityResult(IMAGE_REQUEST, result.resultCode, result.data!!)
        }
      }

    init(toolbarAdd)

    presenter = initPresenter (AddPlaceMarkPresenter(this)) as AddPlaceMarkPresenter

    chooseImage.setOnClickListener {
      presenter.putTextDataBeforeMove(placemarkTitle.text.toString(),description.text.toString())
      presenter.doSelectImage(imagePickerLauncher)
    }

  }

  override fun showPlaceMark(placeMark: PlaceMarkModel) {
    Log.d(TAG, "showPlaceMark: $placeMark")
    placemarkTitle.setText(placeMark.title)
    description.setText(placeMark.description)
    placemarkImage.visibility = View.VISIBLE
    placemarkImage.setImageBitmap(readImageFromPath(this, placeMark.image))
    chooseImage.setText(R.string.select_placemark_image)
    lat.setText("%.6f".format(placeMark.lat))
    lng.setText("%.6f".format(placeMark.lng))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_placemark, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.item_cancel -> {
        presenter.doCancel()
      }
      R.id.item_save -> {
        presenter.doAddOrSave(placemarkTitle.text.toString(), description.text.toString())
      }
      R.id.item_remove -> {
        presenter.doDelete()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    presenter.doCancel()
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
//        presenter.doResartLocationUpdates()
    }

}