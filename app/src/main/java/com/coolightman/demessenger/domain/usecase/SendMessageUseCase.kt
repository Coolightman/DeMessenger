package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(message: Message) = repository.sendMessage(message)
}