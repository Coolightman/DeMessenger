package com.coolightman.demessenger.domain.entity

import androidx.recyclerview.widget.DiffUtil

data class User(
    val userId: String,
    val nickName: String,
    val isOnline: Boolean
) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}
