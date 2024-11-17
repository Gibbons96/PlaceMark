package com.example.placemark.ui

import android.content.Intent
import com.example.placemark.PlaceMarkApp

open class BasePresenter(var baseViewActivity: BaseViewActivity?) {

    var app: PlaceMarkApp =  baseViewActivity?.application as PlaceMarkApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    }

    open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

    open fun onDestroy() {
        baseViewActivity = null
    }
}