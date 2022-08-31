package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UserRepository
import javax.inject.Inject

class SetUserIsOnlineUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, isOnline: Boolean) =
        repository.setUserIsOnline(userId, isOnline)
}