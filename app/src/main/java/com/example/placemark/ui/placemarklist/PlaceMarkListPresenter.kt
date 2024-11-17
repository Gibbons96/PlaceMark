package com.example.placemark.ui.placemarklist

import android.content.Intent
import com.example.placemark.models.PlaceMarkModel
import com.example.placemark.ui.BasePresenter
import com.example.placemark.ui.BaseViewActivity
import com.example.placemark.ui.VIEW
import com.example.placemark.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth

class PlaceMarkListPresenter(view: BaseViewActivity) : BasePresenter(view) {

    fun doAddPlaceMark() {
        baseViewActivity?.navigateTo(VIEW.PLACE_MARK)
    }

    fun doEditPlaceMark(placeMark: PlaceMarkModel) {
        baseViewActivity?.navigateTo(VIEW.PLACE_MARK, 0, "placemark_edit", placeMark)
    }

    fun doShowPlaceMarksMap() {
        baseViewActivity?.navigateTo(VIEW.MAPS)
    }

    fun loadPlaceMarks() {
        baseViewActivity?.showPlaceMarks(app.placemarks.findAll())
    }

    fun doLogout(placeMarkListActivity: PlaceMarkListActivity) {
        app.placemarks.deleteEveryThing()
        FirebaseAuth.getInstance().signOut()
        Intent(placeMarkListActivity, AuthActivity::class.java).also {
            placeMarkListActivity.startActivity(it)
        }
        placeMarkListActivity.finish()
    }
}