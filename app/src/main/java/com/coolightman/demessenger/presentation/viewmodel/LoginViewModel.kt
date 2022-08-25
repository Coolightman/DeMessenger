package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    init {
        authentication()
    }

    private fun authentication() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) enterApp(currentUser)
        else Log.d(LOG_TAG, "User is NOT authorized")
    }

    fun login(email: String, password: String) {
        if (isNotEmptyFields(password, email)) {
            signInFirebase(email, password)
        } else {
            _toast.postValue("Some fields are empty")
        }
    }

    private fun signInFirebase(userEmail: String, userPassword: String) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "signInWithEmailAndPassword:success")
                    enterApp(firebaseAuth.currentUser)
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "signInWithEmailAndPassword:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
            }
    }

    private fun enterApp(currentUser: FirebaseUser?) {
        currentUser?.let {
            _user.postValue(it)
        }
    }

    private fun isNotEmptyFields(password: String, email: String) =
        password.isNotEmpty() && email.isNotEmpty()

    fun resetToast() {
        _toast.postValue("")
        _toastLong.postValue("")
    }

    companion object {
        private const val LOG_TAG = "LoginViewModel"
    }

}