package com.coolightman.demessenger.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.coolightman.demessenger.databinding.FragmentChatBinding
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.presentation.adapter.MessagesAdapter
import com.coolightman.demessenger.presentation.viewmodel.ChatViewModel

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val args by navArgs<ChatFragmentArgs>()

    private val currentUserId by lazy {
        args.currentUserId
    }
    private val companionUserId by lazy {
        args.companionUserId
    }

    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        observers()
        listeners()
    }

    private fun listeners() {

    }

    private fun observers() {

    }

    private fun createAdapter() {
        messagesAdapter = MessagesAdapter(currentUserId)
        binding.rvChat.apply {
            layoutManager = setLayoutManager()
            adapter = messagesAdapter
        }
    }

    private fun setLayoutManager() =
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            .apply {
                stackFromEnd = true
                isSmoothScrollbarEnabled = true
            }

}