package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(email: String, password: String) = repository.loginUser(email, password)
}