package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.Message

interface MessageRepository {
    fun getChatMessages(userId: String, companionId: String)
    fun sendMessage(message: Message)
}