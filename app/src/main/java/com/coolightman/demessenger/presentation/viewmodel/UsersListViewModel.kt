package com.coolightman.demessenger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersListViewModel : ViewModel() {

    private val firebase = FirebaseAuth.getInstance()

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    private val _isRestartApp = MutableLiveData<Boolean>()
    val isRestartApp: LiveData<Boolean>
        get() = _isRestartApp

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            firebase.signOut()
            delay(SIGN_OUT_PAUSE)
            _isRestartApp.postValue(true)
        }
    }

    companion object {
        private const val LOG_TAG = "UsersListViewModel"
        private const val SIGN_OUT_PAUSE = 500L
    }
}