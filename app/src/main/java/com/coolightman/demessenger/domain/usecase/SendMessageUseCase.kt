package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.repository.MessageRepository

class SendMessageUseCase(
    private val repository: MessageRepository
) {
    operator fun invoke(message: Message) = repository.sendMessage(message)
}