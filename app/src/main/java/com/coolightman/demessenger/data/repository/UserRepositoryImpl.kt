package com.coolightman.demessenger.data.repository

import android.util.Log
import com.coolightman.demessenger.data.database.USERS_REF
import com.coolightman.demessenger.data.database.USER_IS_ONLINE_KEY
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named(USERS_REF) private val referenceUsers: DatabaseReference
) : UserRepository {

    override suspend fun setUserIsOnline(userId: String, online: Boolean) {
        withContext(Dispatchers.IO) {
            referenceUsers.child(userId).child(USER_IS_ONLINE_KEY).setValue(online)
        }
    }

    override suspend fun logoutUser() {
        withContext(Dispatchers.IO) {
            firebaseAuth.signOut()
            Log.d(LOG_TAG, "Logout User")
        }
    }

    override suspend fun resetPassword(email: String): Flow<ResultOf<Boolean>> =
        callbackFlow {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "sendPasswordResetEmail:success")
                    trySend(ResultOf.Success(true))
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "sendPasswordResetEmail:failure | ${it.message}")
                        trySend(ResultOf.Error(it.message ?: "null error message"))
                    }
                }
            }
            awaitClose()
        }.flowOn(Dispatchers.IO).take(1)

    override suspend fun registerUser(
        nickname: String,
        email: String,
        password: String
    ): Flow<ResultOf<Boolean>> = callbackFlow {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                    launch { addUserInFDB(nickname) }
                    trySend(ResultOf.Success(true))
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "createUserWithEmail:failure | ${it.message}")
                        trySend(ResultOf.Error(it.message ?: "null error message"))
                    }
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO).take(1)

    private suspend fun addUserInFDB(nickname: String) {
        withContext(Dispatchers.IO) {
            val firebaseUser = getCurrentUser()
            firebaseUser?.let {
                val user = User(firebaseUser.uid, nickname)
                referenceUsers.child(firebaseUser.uid).setValue(user)
            }
        }
    }

    override suspend fun getCurrentUser(): FirebaseUser? =
        withContext(Dispatchers.IO) {
            firebaseAuth.currentUser
        }

    override suspend fun loginUser(email: String, password: String): Flow<ResultOf<Boolean>> =
        callbackFlow {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(LOG_TAG, "signInWithEmailAndPassword:success")
                        trySend(ResultOf.Success(true))
                    } else {
                        task.exception?.let {
                            Log.d(LOG_TAG, "signInWithEmailAndPassword:failure | ${it.message}")
                            trySend(ResultOf.Error(it.message ?: "null error message"))
                        }
                    }
                }
            awaitClose()
        }.flowOn(Dispatchers.IO).take(1)

    override suspend fun getUser(userId: String): Flow<ResultOf<User>> = callbackFlow {
        referenceUsers.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    trySend(ResultOf.Success(it))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, "Get user data:failure | ${error.message}")
                trySend(ResultOf.Error(error.message))
            }
        })
        awaitClose()
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val LOG_TAG = "UserRepositoryImpl"
    }
}