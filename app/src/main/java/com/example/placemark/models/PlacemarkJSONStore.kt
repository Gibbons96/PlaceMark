package com.example.placemark.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.example.placemark.helpers.*
import java.util.*

val JSON_FILE = "placemarks.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<PlaceMarkModel>>() {}.type

fun generateRandomId(): Long {
  return Random().nextLong()
}

class PlaceMarkJSONStore : PlaceMarkStore{

  val context: Context
  var placemarks = mutableListOf<PlaceMarkModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, JSON_FILE)) {
      deserialize()
    }
  }

  override fun findAll(): MutableList<PlaceMarkModel> {
    return placemarks
  }

  override fun create(placemark: PlaceMarkModel) {
    placemark.id = generateRandomId()
    placemarks.add(placemark)
    serialize()
  }


  override fun update(placemark: PlaceMarkModel) {
    var foundPlaceMark: PlaceMarkModel? = placemarks.find { p -> p.id == placemark.id }
    if (foundPlaceMark != null){
      foundPlaceMark.title = placemark.title
      foundPlaceMark.description = placemark.description
      foundPlaceMark.image = placemark.image
      foundPlaceMark.lat = placemark.lat
      foundPlaceMark.lng = placemark.lng
      foundPlaceMark.zoom = placemark.zoom
      serialize()
    }
  }

  override fun findById(id: Long): PlaceMarkModel {
    return placemarks.find { p -> p.id == id }!!
  }


  override fun delete(placemark: PlaceMarkModel) {
    placemarks.remove(placemark)
    serialize()
  }

  override fun deleteEveryThing() {
    placemarks.clear()
    deleteFile(context, JSON_FILE)
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(placemarks, listType)
    write(context, JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_FILE)
    placemarks = Gson().fromJson(jsonString, listType)
  }


}