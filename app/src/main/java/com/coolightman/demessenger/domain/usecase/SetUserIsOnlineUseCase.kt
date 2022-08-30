package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository

class SetUserIsOnlineUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(isOnline: Boolean) = repository.setUserIsOnline(isOnline)
}