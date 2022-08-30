package com.coolightman.demessenger.domain.usecase

import com.coolightman.demessenger.domain.repository.UsersRepository

class GetAllUsersUseCase(
    private val repository: UsersRepository
) {
    operator fun invoke() = repository.getAllUsersList()
}