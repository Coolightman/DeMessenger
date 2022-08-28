package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.utils.DB_URL
import com.coolightman.demessenger.utils.MESSAGES_REF
import com.coolightman.demessenger.utils.USERS_REF
import com.coolightman.demessenger.utils.USER_IS_ONLINE_KEY
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatViewModel(
    private val currentUserId: String,
    private val companionUserId: String
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance(DB_URL)
    private val referenceUsers = firebaseDB.getReference(USERS_REF)
    private val referenceMessages = firebaseDB.getReference(MESSAGES_REF)

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

    init {
        listenCompanionUserData()
        listenMessages()
    }

    fun setCurrentUserIsOnline(isOnline: Boolean){
        referenceUsers.child(currentUserId).child(USER_IS_ONLINE_KEY).setValue(isOnline)
    }

    private fun listenMessages() {
        referenceMessages.child(currentUserId).child(companionUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = mutableListOf<Message>()
                for (dataSnapshot in snapshot.children){
                    val message = dataSnapshot.getValue(Message::class.java)
                    message?.let {
                        messagesList.add(it)
                    }
                }
                _messages.postValue(messagesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, "listen chat messages:failure | ${error.message}")
                _toastLong.postValue(error.message)
            }
        })
    }

    private fun listenCompanionUserData() {
        referenceUsers.child(companionUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    _companionUser.postValue(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, "read companionUser User:failure | ${error.message}")
                _toastLong.postValue(error.message)
            }
        })
    }

    fun sendMessage(message: Message) {
        referenceMessages
            .child(message.senderId)
            .child(message.receiverId)
            .push()
            .setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "sendMessage:success")
                    saveMessageToReceiver(message)
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "sendMessage:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
            }
    }

    private fun saveMessageToReceiver(message: Message) {
        referenceMessages
            .child(message.receiverId)
            .child(message.senderId)
            .push()
            .setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "saveMessageToReceiver:success")
                    _isSentMessage.postValue(true)
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "saveMessageToReceiver:failure | ${it.message}")
                        _toastLong.postValue(it.message)
                    }
                }
            }
    }

    companion object {
        private const val LOG_TAG = "ChatViewModel"
    }

}