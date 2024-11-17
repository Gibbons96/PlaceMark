package com.example.placemark.ui.placemarklist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_placemark_list.*
import com.example.placemark.R
import com.example.placemark.models.PlaceMarkModel
import com.example.placemark.ui.BaseViewActivity

class PlaceMarkListActivity :  BaseViewActivity(), PlaceMarkListener {

  private val TAG: String = "PlaceMarkListActivity"
  lateinit var presenter: PlaceMarkListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_list)
    init(toolbarMain)
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
    }
    presenter = initPresenter(PlaceMarkListPresenter(this)) as PlaceMarkListPresenter

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    presenter.loadPlaceMarks()
  }

  @SuppressLint("NotifyDataSetChanged")
  override fun showPlaceMarks(placeMarks: List<PlaceMarkModel>) {
    recyclerView.adapter = PlaceMarkAdapter(placeMarks, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    Log.d(TAG, "onOptionsItemSelected: ${item.itemId}")
    when (item.itemId) {
      R.id.item_add -> presenter.doAddPlaceMark()
      R.id.item_map -> presenter.doShowPlaceMarksMap()
      R.id.item_logout -> presenter.doLogout(this)
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onPlaceMarkClick(placeMark: PlaceMarkModel) {
    presenter.doEditPlaceMark(placeMark)
  }

  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadPlaceMarks()
    super.onActivityResult(requestCode, resultCode, data)
  }
}