package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.utils.DB_URL
import com.coolightman.demessenger.utils.USERS_REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersListViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseDatabase.getInstance(DB_URL)

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>>
        get() = _usersList

    private val _isRestartApp = MutableLiveData<Boolean>()
    val isRestartApp: LiveData<Boolean>
        get() = _isRestartApp

    init {
        loadUsersList()
    }

    private fun loadUsersList() {
        val usersReference = firebaseDB.getReference(USERS_REF)

        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersFromDb = mutableListOf<User>()

                firebaseAuth.currentUser?.let { currentUser ->
                    for (dataSnapshot in snapshot.children) {
                        val user = dataSnapshot.getValue(User::class.java)
                        user?.let {
                            if (it.userId != currentUser.uid) {
                                usersFromDb.add(user)
                            }
                        }
                    }
                    _usersList.postValue(usersFromDb)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, "loadUsersList:onCancelled | ${error.message}")
                firebaseAuth.currentUser?.let { _toastLong.postValue(error.message) }
            }
        })
    }

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signOut()
            Log.d(LOG_TAG, "Logout current User")
            delay(SIGN_OUT_PAUSE)
            restartApplication()
        }
    }

    private fun restartApplication() {
        _isRestartApp.postValue(true)
    }

    companion object {
        private const val LOG_TAG = "UsersListViewModel"
        private const val SIGN_OUT_PAUSE = 500L
    }
}