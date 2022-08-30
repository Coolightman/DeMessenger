package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coolightman.demessenger.domain.usecase.GetFirebaseUserUseCase
import com.coolightman.demessenger.domain.usecase.LoginUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getFirebaseUserUseCase: GetFirebaseUserUseCase,
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?>
        get() = _userId

    init {
        authentication()
    }

    private fun authentication() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _userId.postValue(currentUser.uid)
        } else {
            Log.d(LOG_TAG, "User is NOT authorized")
            _userId.postValue(null)
        }
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
                    authentication()
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "signInWithEmailAndPassword:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
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