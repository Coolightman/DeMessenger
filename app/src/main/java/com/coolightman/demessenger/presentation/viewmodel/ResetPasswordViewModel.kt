package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private var _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private var _isSuccessReset = MutableLiveData<Boolean>()
    val isSuccessReset: LiveData<Boolean>
        get() = _isSuccessReset

    fun resetPassword(userEmail: String) {
        if (userEmail.isNotEmpty()) {
            resetPasswordFirebase(userEmail)
        } else {
            _toast.postValue("Some fields are empty")
        }
    }

    private fun resetPasswordFirebase(userEmail: String) {
        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "sendPasswordResetEmail:success")
                _toast.postValue("Reset password email is sent")
                _isSuccessReset.postValue(true)
            } else {
                task.exception?.let {
                    Log.d(LOG_TAG, "sendPasswordResetEmail:failure | ${it.message}")
                    _toastLong.postValue(it.message)
                }
            }
        }
    }

    companion object {
        private const val LOG_TAG = "ResetPasswordViewModel"
    }
}