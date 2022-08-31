package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.ResultOf
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getChatMessages(userId: String, companionId: String): Flow<ResultOf<List<Message>>>
    suspend fun sendMessage(message: Message): Flow<ResultOf<Boolean>>
}