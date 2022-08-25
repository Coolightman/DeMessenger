package com.coolightman.demessenger.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coolightman.demessenger.R
import com.coolightman.demessenger.databinding.UserItemBinding
import com.coolightman.demessenger.domain.entity.User
import com.google.android.material.textview.MaterialTextView

class UsersAdapter(private val clickListener: (String) -> Unit) :
    ListAdapter<User, UsersAdapter.UserViewHolder>(User.DIFF) {

    class UserViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.binding.apply {
            setNickName(tvUserInfo, user.nickName)
            setOnlineStatus(viewOnlineStatus, user.isOnline, holder)
            root.setOnClickListener { clickListener(user.userId) }
        }
    }

    private fun setOnlineStatus(viewOnlineStatus: View, isOnline: Boolean, holder: UserViewHolder) {
        val bgResId = when (isOnline) {
            true -> R.drawable.circle_green
            false -> R.drawable.circle_red
        }
        val bg = ContextCompat.getDrawable(holder.itemView.context, bgResId)
        viewOnlineStatus.background = bg
    }

    private fun setNickName(tvUserInfo: MaterialTextView, nickName: String) {
        tvUserInfo.text = nickName
    }
}