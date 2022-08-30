package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UsersRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke() = repository.getAllUsersList()
}