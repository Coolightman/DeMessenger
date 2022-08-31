package com.coolightman.demessenger.presentation.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.coolightman.demessenger.R
import com.coolightman.demessenger.databinding.FragmentChatBinding
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.presentation.adapter.MessagesAdapter
import com.coolightman.demessenger.presentation.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ChatViewModel>()

    private val args by navArgs<ChatFragmentArgs>()

    private val userId by lazy {
        args.userId
    }
    private val companionId by lazy {
        args.companionId
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

    override fun onResume() {
        super.onResume()
        viewModel.setUserIsOnline(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUserIsOnline(false)
    }

    private fun listeners() {
        binding.apply {
            viewSendMessage.setOnClickListener {
                val text = etMessage.text.toString().trim()
                if (text.isNotEmpty()) {
                    viewModel.sendMessage(text)
                }
            }

            topAppToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

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
                binding.rvChat.smoothScrollToPosition(it.size)
            }

            companion.observe(viewLifecycleOwner) {
                it?.let {
                    val isOnlineBackground = getIsOnlineBackground(it)
                    binding.apply {
                        topAppToolbar.title = it.nickName
                        viewOnlineStatus.background = isOnlineBackground
                    }
                }
            }

            isSentMessage.observe(viewLifecycleOwner) {
                if (it) {
                    binding.etMessage.text?.clear()
                }
            }

        }
    }

    private fun getIsOnlineBackground(it: User): Drawable? {
        return when (it.online) {
            true -> ContextCompat.getDrawable(requireContext(), R.drawable.circle_green)
            false -> ContextCompat.getDrawable(requireContext(), R.drawable.circle_red)
        }
    }

    private fun createAdapter() {
        messagesAdapter = MessagesAdapter(userId)
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