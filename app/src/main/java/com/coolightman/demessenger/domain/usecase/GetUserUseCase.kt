package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository

class GetUserUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String) = repository.getUser(userId)
}