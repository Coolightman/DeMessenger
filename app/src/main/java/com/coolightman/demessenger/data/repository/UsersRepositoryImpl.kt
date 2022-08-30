package com.coolightman.demessenger.data.repository

import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.repository.UsersRepository
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val firebaseDB: FirebaseDatabase
) : UsersRepository {
    override fun getAllUsersList(): List<User> {
        TODO("Not yet implemented")
    }
}