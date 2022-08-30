package com.coolightman.demessenger.data.repository

import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDB: FirebaseDatabase
): UserRepository {

    override fun setUserIsOnline(online: Boolean) {
        TODO("Not yet implemented")
    }

    override fun logoutUser() {
        TODO("Not yet implemented")
    }

    override fun resetPassword(email: String) {
        TODO("Not yet implemented")
    }

    override fun registerUser(nickname: String, email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): FirebaseUser? {
        TODO("Not yet implemented")
    }

    override fun loginUser(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): User {
        TODO("Not yet implemented")
    }
}