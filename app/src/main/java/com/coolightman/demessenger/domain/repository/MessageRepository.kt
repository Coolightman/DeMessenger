package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.Message

interface MessageRepository {
    suspend fun getChatMessages(userId: String, companionId: String)
    suspend fun sendMessage(message: Message)
}