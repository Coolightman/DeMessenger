package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UsersRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(userId: String) = repository.getAllUsersList(userId)
}