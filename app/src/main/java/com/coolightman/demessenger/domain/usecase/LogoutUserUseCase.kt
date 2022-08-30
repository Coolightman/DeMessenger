package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository

class LogoutUserUseCase(
    private val repository: UserRepository
) {
    operator fun invoke() = repository.logoutUser()
}