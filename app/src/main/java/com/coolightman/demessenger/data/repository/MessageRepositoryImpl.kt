package com.coolightman.demessenger.data.repository

import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.repository.MessageRepository
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val firebaseDB: FirebaseDatabase
) : MessageRepository {

    override fun getChatMessages(userId: String, companionId: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: Message) {
        TODO("Not yet implemented")
    }
}