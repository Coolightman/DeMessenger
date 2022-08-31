package com.coolightman.demessenger.data.repository

import android.util.Log
import com.coolightman.demessenger.data.database.USERS_REF
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.repository.UsersRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class UsersRepositoryImpl @Inject constructor(
    @Named(USERS_REF) private val referenceUsers: DatabaseReference
) : UsersRepository {

    override suspend fun getAllUsersList(userId: String): Flow<ResultOf<List<User>>> =
        callbackFlow {
            referenceUsers.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val otherUsers = mutableListOf<User>()

                    for (dataSnapshot in snapshot.children) {
                        val user = dataSnapshot.getValue(User::class.java)
                        user?.let {
                            if (it.userId != userId) {
                                otherUsers.add(user)
                            }
                        }
                    }
                    trySend(ResultOf.Success(otherUsers))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(LOG_TAG, "loadUsersList:onCancelled | ${error.message}")
                    trySend(ResultOf.Error(error.message))
                }
            })
            awaitClose()
        }.flowOn(Dispatchers.IO)

    companion object {
        private const val LOG_TAG = "UsersRepositoryImpl"
    }
}