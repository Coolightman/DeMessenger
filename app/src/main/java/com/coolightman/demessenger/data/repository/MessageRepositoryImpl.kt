package com.coolightman.demessenger.data.repository

import android.util.Log
import com.coolightman.demessenger.data.database.MESSAGES_REF
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.repository.MessageRepository
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
import javax.inject.Inject
import javax.inject.Named

class MessageRepositoryImpl @Inject constructor(
    @Named(MESSAGES_REF) private val referenceMessages: DatabaseReference
) : MessageRepository {

    override suspend fun getChatMessages(
        userId: String,
        companionId: String
    ): Flow<ResultOf<List<Message>>> = callbackFlow {
        referenceMessages.child(userId).child(companionId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messagesList = mutableListOf<Message>()
                    for (dataSnapshot in snapshot.children) {
                        val message = dataSnapshot.getValue(Message::class.java)
                        message?.let {
                            messagesList.add(it)
                        }
                    }
                    trySend(ResultOf.Success(messagesList))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(LOG_TAG, "Get chat messages:failure | ${error.message}")
                    trySend(ResultOf.Error(error.message))
                }
            })
        awaitClose()
    }.flowOn(Dispatchers.IO)

    override suspend fun sendMessage(message: Message): Flow<ResultOf<Boolean>> = callbackFlow {
        referenceMessages
            .child(message.senderId)
            .child(message.receiverId)
            .push()
            .setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "saveMessageToSender:success")
                    referenceMessages
                        .child(message.receiverId)
                        .child(message.senderId)
                        .push()
                        .setValue(message)
                        .addOnCompleteListener { receiverTask ->
                            if (receiverTask.isSuccessful) {
                                Log.d(LOG_TAG, "saveMessageToReceiver:success")
                                trySend(ResultOf.Success(true))
                            } else {
                                receiverTask.exception?.let {
                                    Log.d(LOG_TAG, "saveMessageToReceiver:failure | ${it.message}")
                                    trySend(ResultOf.Error(it.message ?: "null error message"))
                                }
                            }
                        }
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "saveMessageToSender:failure | ${it.message}")
                        trySend(ResultOf.Error(it.message ?: "null error message"))
                    }
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO).take(1)


    companion object {
        private const val LOG_TAG = "MessageRepositoryImpl"
    }
}