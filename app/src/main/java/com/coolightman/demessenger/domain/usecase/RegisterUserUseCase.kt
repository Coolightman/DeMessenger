package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(nickname: String, email: String, password: String) =
        repository.registerUser(nickname, email, password)
}