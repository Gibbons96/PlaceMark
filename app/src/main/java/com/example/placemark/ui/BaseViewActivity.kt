package com.example.placemark.ui

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.example.placemark.models.PlaceMarkModel
import com.example.placemark.ui.editlocation.EditLocationViewActivity
import com.example.placemark.ui.map.PlaceMarkMapsViewActivity
import com.example.placemark.ui.addPlaceMark.AddPlaceMarkActivity
import com.example.placemark.ui.placemarklist.PlaceMarkListActivity

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, PLACE_MARK, MAPS, LIST
}

open abstract class BaseViewActivity() : AppCompatActivity() {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, PlaceMarkListActivity::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationViewActivity::class.java)
            VIEW.PLACE_MARK -> intent = Intent(this, AddPlaceMarkActivity::class.java)
            VIEW.MAPS -> intent = Intent(this, PlaceMarkMapsViewActivity::class.java)
            VIEW.LIST -> intent = Intent(this, PlaceMarkListActivity::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showPlaceMark(placeMark: PlaceMarkModel) {}
    open fun showPlaceMarks(placeMarks: List<PlaceMarkModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}