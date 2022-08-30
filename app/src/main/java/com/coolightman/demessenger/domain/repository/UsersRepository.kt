package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.User

interface UsersRepository {
    suspend fun getAllUsersList(): List<User>
}