package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getAllUsersList(userId: String): Flow<ResultOf<List<User>>>
}