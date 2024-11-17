package com.example.placemark.repositories

import com.example.placemark.data.entities.Marker
import com.google.firebase.auth.AuthResult
import com.example.placemark.others.Resource

interface AuthRepository {
    suspend fun register(email: String, name: String, password: String): Resource<AuthResult>
    suspend fun login(email: String, password: String): Resource<AuthResult>
    suspend fun saveMarkerData(uid: String, marker: Marker): Resource<Unit>

}