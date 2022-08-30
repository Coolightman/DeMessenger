package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun setUserIsOnline(online: Boolean)
    fun logoutUser()
    fun resetPassword(email: String)
    fun registerUser(nickname: String, email: String, password: String)
    fun getCurrentUser(): FirebaseUser?
    fun loginUser(email: String, password: String)
    fun getUser(userId: String): User
}