package com.example.placemark.ui.addPlaceMark

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.placemark.helpers.checkLocationPermissions
import com.example.placemark.helpers.createDefaultLocationRequest
import com.example.placemark.helpers.isPermissionGranted
import com.example.placemark.helpers.showImagePicker
import com.example.placemark.models.Location
import com.example.placemark.models.PlaceMarkModel
import com.example.placemark.ui.BasePresenter
import com.example.placemark.ui.BaseViewActivity
import com.example.placemark.ui.IMAGE_REQUEST
import com.example.placemark.ui.LOCATION_REQUEST
import com.example.placemark.ui.VIEW
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class AddPlaceMarkPresenter(activity: BaseViewActivity) : BasePresenter(activity) {

    private val TAG: String = "AddPlaceMarkPresenter"
    var placeMark = PlaceMarkModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var map: GoogleMap? = null
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    val locationRequest = createDefaultLocationRequest()

    init {
        if (activity.intent.hasExtra("placemark_edit")) {
            edit = true
            placeMark = activity.intent.extras?.getParcelable<PlaceMarkModel>("placemark_edit")!!
            activity.showPlaceMark(placeMark)
        } else {
            if (checkLocationPermissions(activity)) {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            Log.d(TAG, "locationUpdate: 1")
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    Log.d(TAG, "locationUpdate: 2")
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            Log.d(TAG, "locationUpdate: 3")
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }


    fun doAddOrSave(title: String, description: String) {
        placeMark.title = title
        placeMark.description = description
        if (edit) {
            app.placemarks.update(placeMark)
        } else {
            app.placemarks.create(placeMark)
        }
        baseViewActivity?.finish()
    }

    fun doCancel() {
        baseViewActivity?.finish()
    }

    fun doDelete() {
        app.placemarks.delete(placeMark)
        baseViewActivity?.finish()
    }

    fun doSelectImage(
        imagePickerLauncher: ActivityResultLauncher<Intent>
    ) {
        baseViewActivity?.let{
            showImagePicker(baseViewActivity!!, IMAGE_REQUEST,imagePickerLauncher)
        }
    }

    fun putTextDataBeforeMove(
        titleString: String,
        descString: String
    ){
        placeMark.title = titleString
        placeMark.description = descString
    }

    fun doSetLocation() {
//        if (edit == false) {
            baseViewActivity?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placeMark.lat, placeMark.lng, placeMark.zoom))
//        } else {
//            baseViewActivity?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placeMark.lat, placeMark.lng, placeMark.zoom))
//        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d(TAG, "doActivityResult: ")
        when (requestCode) {
            IMAGE_REQUEST -> {
                Log.d(TAG, "doActivityResult: IMAGE_REQUEST")
                data.data?.let {
                    val savedUri = copyImageToInternalStorage(it) // Save the image locally
                    savedUri?.let { localUri ->
                        Log.d(TAG, "doActivityResult: $localUri")
                    }
                }
                Log.d(TAG, "doActivityResult: ${data.data.toString()}")
                placeMark.image = data.data.toString()
                baseViewActivity?.showPlaceMark(placeMark)
            }
            LOCATION_REQUEST -> {
                Log.d(TAG, "doActivityResult: LOCATION_REQUEST ")
                val location = data.extras?.getParcelable<Location>("location")
                if (location != null) {
                    placeMark.lat = location.lat
                    placeMark.lng = location.lng
                    placeMark.zoom = location.zoom
                    Log.d(TAG, "locationUpdate: 4")
                    locationUpdate(location.lat,location.lng)
                }

            }
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        Log.d(TAG, "locationUpdate: 5")
        locationUpdate(placeMark.lat, placeMark.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        Log.d(TAG, "locationUpdate: ")
        placeMark.lat = lat
        placeMark.lng = lng
        placeMark.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(placeMark.title).position(LatLng(placeMark.lat, placeMark.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeMark.lat, placeMark.lng), placeMark.zoom))
        baseViewActivity?.showPlaceMark(placeMark)

    }

    private fun copyImageToInternalStorage(uri: Uri): Uri? {
        return try {
            val inputStream: InputStream? = baseViewActivity!!.contentResolver.openInputStream(uri)
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val file = File(baseViewActivity!!.filesDir, fileName)
            val outputStream: OutputStream = file.outputStream()

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(file) // Return the URI of the saved file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}