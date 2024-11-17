package com.example.placemark.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import java.io.IOException

val TAG: String = "ImageHelper"

fun showImagePicker(parent: Activity, id: Int, imagePickerLauncher: ActivityResultLauncher<Intent>) {
  val intent = Intent(Intent.ACTION_PICK).apply {
    type = "image/*"
  }
  imagePickerLauncher.launch(intent)
}


fun readImage(activity: Activity, resultCode: Int, data: Intent?): Bitmap? {
  var bitmap: Bitmap? = null
  if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
    try {
      bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, data.data)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
  return bitmap
}

fun readImageFromPath(context: Context, path: String): Bitmap? {
  if(path.isEmpty()) return null
  val uri = Uri.parse(path)
  if (uri == null) {
    Log.e(TAG, "Invalid URI")
    return null
  }

  return try {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    bitmap
  } catch (e: SecurityException) {
    Log.e(TAG, "SecurityException: Unable to read image from path", e)
    null
  } catch (e: Exception) {
    Log.e(TAG, "Exception: Unable to read image from path", e)
    null
  }
}
