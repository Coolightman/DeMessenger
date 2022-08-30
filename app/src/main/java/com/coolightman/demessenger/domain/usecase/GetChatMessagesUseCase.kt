package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.MessageRepository

class GetChatMessagesUseCase(
    private val repository: MessageRepository
) {
    operator fun invoke(userId: String, companionId: String) =
        repository.getChatMessages(userId, companionId)
}