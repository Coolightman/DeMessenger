package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegistrationViewModel : ViewModel() {

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
            createUserFirebase(email, password)
        } else {
            _toast.postValue("Some fields are empty")
        }
    }

    private fun createUserFirebase(userEmail: String, userPassword: String) {
        firebase.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                    sendEmailVerification()
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "createUserWithEmail:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
            }
    }

    private fun sendEmailVerification() {
        val currentUser = firebase.currentUser
        currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "sendEmailVerification:success")
                _isSuccessRegistration.postValue(true)
            } else {
                task.exception?.let {
                    Log.d(LOG_TAG, "sendEmailVerification:failure | ${it.message}")
                    _toastLong.postValue(it.message)
                }
            }
        }
    }

    private fun isNotEmptyFields(nickname: String, email: String, password: String) =
        nickname.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()

    companion object {
        private val firebase = FirebaseAuth.getInstance()
        private const val LOG_TAG = "RegistrationViewModel"
    }
}