package com.example.placemark.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.placemark.R
import com.example.placemark.ui.placemarklist.PlaceMarkListActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d("TAG", "onStart: ${FirebaseAuth.getInstance().currentUser?.uid}")
            Intent(this, PlaceMarkListActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}