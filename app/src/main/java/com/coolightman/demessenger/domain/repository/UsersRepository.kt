package com.coolightman.demessenger.domain.repository

import com.coolightman.demessenger.domain.entity.User

interface UsersRepository {
    fun getAllUsersList(): List<User>
}