package com.coolightman.demessenger.presentation.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.coolightman.demessenger.R
import com.coolightman.demessenger.databinding.FragmentChatBinding
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.presentation.adapter.MessagesAdapter
import com.coolightman.demessenger.presentation.viewmodel.ChatViewModel
import com.coolightman.demessenger.presentation.viewmodel.ChatViewModelFactory

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatViewModel::class.java]
    }

    private val viewModelFactory by lazy {
        ChatViewModelFactory(currentUserId, companionUserId)
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
        binding.apply {
            viewSendMessage.setOnClickListener {
                val text = etMessage.text.toString().trim()
                if (text.isNotEmpty()){
                    val message: Message = createMessage(text)
                    viewModel.sendMessage(message)
                }
            }
        }
    }

    private fun createMessage(text: String): Message =
        Message(
            text = text,
            senderId = currentUserId,
            receiverId = companionUserId
        )

    private fun observers() {
        viewModel.apply {
            toast.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
            }

            toastLong.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
                }
            }

            messages.observe(viewLifecycleOwner) {
                messagesAdapter.submitList(it)
            }

            companionUser.observe(viewLifecycleOwner) {
                it?.let {
                    val isOnlineBackground = getIsOnlineBackground(it)
                    binding.apply {
                        tvCompanionInfo.text = it.nickName
                        viewOnlineStatus.background = isOnlineBackground
                    }
                }
            }

            isSentMessage.observe(viewLifecycleOwner){
                if (it){
                    binding.etMessage.text?.clear()
                }
            }

        }
    }

    private fun getIsOnlineBackground(it: User): Drawable? {
        return when (it.isOnline) {
            true ->  ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
            false -> ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
        }
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