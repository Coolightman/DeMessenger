package com.coolightman.demessenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coolightman.demessenger.utils.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

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
        val currentUser = firebase.currentUser
        if (currentUser == null) {
            Log.d(LOG_TAG, "User is NOT authorized")
        } else checkEmailVerifyAndEnter(currentUser)
    }

    fun login(email: String, password: String) {
        if (isNotEmptyFields(password, email)) {
            if (isEmailValid(email)) {
                signInFirebase(email, password)
            } else {
                _toast.postValue("E-mail is not valid")
            }
        } else {
            _toast.postValue("Some fields are empty")
        }
    }

    private fun signInFirebase(userEmail: String, userPassword: String) {
        firebase.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "signInWithEmailAndPassword:success")
                    checkEmailVerifyAndEnter(firebase.currentUser!!)
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "signInWithEmailAndPassword:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
            }
    }

    private fun checkEmailVerifyAndEnter(currentUser: FirebaseUser) {
        if (!currentUser.isEmailVerified) {
            Log.d(LOG_TAG, "User email is not verified")
            _toast.postValue("Check your e-mail for verification")
        } else {
            Log.d(LOG_TAG, "User is authorized ${currentUser.uid}")
            _user.postValue(currentUser)
        }
    }

    private fun isNotEmptyFields(password: String, email: String) =
        password.isNotEmpty() && email.isNotEmpty()

    fun resetToast() {
        _toast.postValue("")
        _toastLong.postValue("")
    }

    companion object {
        private val firebase = FirebaseAuth.getInstance()
        const val LOG_TAG = "LoginViewModel"
    }

}