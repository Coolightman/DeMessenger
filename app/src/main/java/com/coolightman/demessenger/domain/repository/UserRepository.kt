package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun setUserIsOnline(online: Boolean)
    suspend fun logoutUser()
    suspend fun resetPassword(email: String)
    suspend fun registerUser(nickname: String, email: String, password: String)
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun loginUser(email: String, password: String)
    suspend fun getUser(userId: String): User
}