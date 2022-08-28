package com.coolightman.demessenger.domain.entity

import androidx.recyclerview.widget.DiffUtil

data class Message(
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = ""
) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                val oldStamp =
                    oldItem.senderId + oldItem.receiverId + oldItem.text + System.currentTimeMillis()
                val newStamp =
                    newItem.senderId + newItem.receiverId + newItem.text + System.currentTimeMillis()
                return oldStamp == newStamp
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}
