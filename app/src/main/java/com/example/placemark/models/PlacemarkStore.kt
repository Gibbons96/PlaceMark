package com.example.placemark.models

interface PlaceMarkStore {
  fun findAll(): List<PlaceMarkModel>
  fun create(placemark: PlaceMarkModel)
  fun update(placemark: PlaceMarkModel)
  fun delete(placemark: PlaceMarkModel)
  fun deleteEveryThing()
  fun findById(id: Long): PlaceMarkModel
}