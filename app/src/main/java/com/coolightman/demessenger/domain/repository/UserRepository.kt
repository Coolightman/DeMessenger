package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.entity.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun setUserIsOnline(userId: String, online: Boolean)
    suspend fun logoutUser()
    suspend fun resetPassword(email: String): Flow<ResultOf<Boolean>>
    suspend fun registerUser(
        nickname: String,
        email: String,
        password: String
    ): Flow<ResultOf<Boolean>>

    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun loginUser(email: String, password: String): Flow<ResultOf<Boolean>>
    suspend fun getUser(userId: String): Flow<ResultOf<User>>
}