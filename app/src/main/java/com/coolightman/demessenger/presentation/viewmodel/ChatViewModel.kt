package com.coolightman.demessenger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.utils.DB_URL
import com.coolightman.demessenger.utils.USERS_REF
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel(
    private val currentUserId: String,
    private val companionUserId: String
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance(DB_URL)
    private val referenceUsers = firebaseDB.getReference(USERS_REF)

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>>
        get() = _messages

    private val _companionUser = MutableLiveData<User>()
    val companionUser: LiveData<User>
        get() = _companionUser

    private val _isSentMessage = MutableLiveData<Boolean>()
    val isSentMessage: LiveData<Boolean>
        get() = _isSentMessage


}