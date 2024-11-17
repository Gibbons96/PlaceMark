package com.example.placemark.repositories

import com.example.placemark.data.entities.Marker
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.placemark.data.entities.User
import com.example.placemark.others.Resource
import com.example.placemark.others.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultAuthRepository : AuthRepository {

    val auth = FirebaseAuth.getInstance()
    private val usersRef = FirebaseFirestore.getInstance().collection("users")

    override suspend fun register(
        email: String,
        name: String,
        password: String
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = User(uid = uid, name = name)
                usersRef.document(uid).set(user).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun saveMarkerData(uid: String, marker: Marker): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            safeCall {
                // Save marker data in the "markers" subcollection of the user's document
                val markerRef = usersRef.document(uid).collection("markers").document()
                markerRef.set(marker).await() // Save the marker data
                Resource.Success(Unit) // Return success without data
            }
        }
    }
}