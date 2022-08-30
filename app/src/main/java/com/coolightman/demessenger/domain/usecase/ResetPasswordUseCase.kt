package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository

class ResetPasswordUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(email: String) = repository.resetPassword(email)
}