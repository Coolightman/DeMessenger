package com.coolightman.demessenger.domain.entity

import androidx.recyclerview.widget.DiffUtil

data class Message(
    val messageId: String = "",
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = ""
) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.messageId == newItem.messageId
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}
