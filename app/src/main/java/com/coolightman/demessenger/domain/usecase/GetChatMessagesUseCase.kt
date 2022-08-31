package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.MessageRepository
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(userId: String, companionId: String) =
        repository.getChatMessages(userId, companionId)
}