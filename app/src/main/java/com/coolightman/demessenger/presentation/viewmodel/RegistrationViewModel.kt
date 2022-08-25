package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coolightman.demessenger.domain.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseDatabase.getInstance("https://demessenger-d355f-default-rtdb.europe-west1.firebasedatabase.app/")

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private var _isSuccessRegistration = MutableLiveData<Boolean>()
    val isSuccessRegistration: LiveData<Boolean>
        get() = _isSuccessRegistration

    fun registerUser(nickname: String, email: String, password: String) {
        if (isNotEmptyFields(nickname, email, password)) {
            createUserFirebase(nickname, email, password)
        } else {
            _toast.postValue("Some fields are empty")
        }
    }

    private fun createUserFirebase(nickname: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                    addUserInFDB(nickname)
                    _toast.postValue("Your account created successfully")
                    _isSuccessRegistration.postValue(true)
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "createUserWithEmail:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
            }
    }

    private fun addUserInFDB(nickname: String) {
        val firebaseUser = firebaseAuth.currentUser
        firebaseUser?.let {
            val user = User(firebaseUser.uid, nickname)
            firebaseDB.getReference("users").child(firebaseUser.uid).setValue(user)
        }
    }

    private fun isNotEmptyFields(nickname: String, email: String, password: String) =
        nickname.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()

    companion object {
        private const val LOG_TAG = "RegistrationViewModel"
    }
}