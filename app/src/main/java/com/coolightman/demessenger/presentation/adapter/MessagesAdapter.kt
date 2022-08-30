package com.coolightman.demessenger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.coolightman.demessenger.databinding.ForeignMessageItemBinding
import com.coolightman.demessenger.databinding.OwnMessageItemBinding
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.MessageType

class MessagesAdapter(
    private val userId: String
) : ListAdapter<Message, MessagesAdapter.MessageViewHolder>(Message.DIFF) {

    private lateinit var bindingOwnMessage: OwnMessageItemBinding
    private lateinit var bindingForeignMessage: ForeignMessageItemBinding

    class MessageViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        when (viewType) {
            MessageType.OWN.ordinal -> {
                bindingOwnMessage = OwnMessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MessageViewHolder(bindingOwnMessage)
            }
            MessageType.FOREIGN.ordinal -> {
                bindingForeignMessage = ForeignMessageItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
                return MessageViewHolder(bindingForeignMessage)
            }
            else -> throw IllegalArgumentException("Wrong message type")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        when (holder.itemViewType) {
            MessageType.OWN.ordinal -> {
                (holder.binding as OwnMessageItemBinding).tvOwnMessage.text = message.text
            }
            else -> {
                (holder.binding as ForeignMessageItemBinding).tvForeignMessage.text = message.text
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).senderId) {
            userId -> MessageType.OWN.ordinal
            else -> MessageType.FOREIGN.ordinal
        }
    }
}