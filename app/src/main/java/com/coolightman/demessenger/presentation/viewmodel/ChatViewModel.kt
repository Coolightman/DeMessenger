package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.coolightman.demessenger.data.database.DB_URL
import com.coolightman.demessenger.data.database.MESSAGES_REF
import com.coolightman.demessenger.data.database.USERS_REF
import com.coolightman.demessenger.data.database.USER_IS_ONLINE_KEY
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.usecase.GetChatMessagesUseCase
import com.coolightman.demessenger.domain.usecase.GetUserUseCase
import com.coolightman.demessenger.domain.usecase.SendMessageUseCase
import com.coolightman.demessenger.domain.usecase.SetUserIsOnlineUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userIsOnlineUseCase: SetUserIsOnlineUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance(DB_URL)
    private val referenceUsers = firebaseDB.getReference(USERS_REF)
    private val referenceMessages = firebaseDB.getReference(MESSAGES_REF)

    private val userId by lazy {
        state.get<String>("userId")!!
    }
    private val companionId by lazy {
        state.get<String>("companionId")!!
    }

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>>
        get() = _messages

    private val _companion = MutableLiveData<User>()
    val companion: LiveData<User>
        get() = _companion

    private val _isSentMessage = MutableLiveData<Boolean>()
    val isSentMessage: LiveData<Boolean>
        get() = _isSentMessage

    init {
        getCompanionData()
        listenMessages()
    }

    fun setUserIsOnline(isOnline: Boolean) {
        referenceUsers.child(userId).child(USER_IS_ONLINE_KEY).setValue(isOnline)
    }

    private fun listenMessages() {
        referenceMessages.child(userId).child(companionId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messagesList = mutableListOf<Message>()
                    for (dataSnapshot in snapshot.children) {
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

    private fun getCompanionData() {
        referenceUsers.child(companionId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    _companion.postValue(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, "Get companion User:failure | ${error.message}")
                _toastLong.postValue(error.message)
            }
        })
    }

    fun sendMessage(text: String) {
        val message = createMessage(text)
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

    private fun createMessage(text: String): Message =
        Message(text, userId, companionId)

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