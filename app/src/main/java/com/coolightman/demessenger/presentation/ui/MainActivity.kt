package com.coolightman.demessenger.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coolightman.demessenger.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}