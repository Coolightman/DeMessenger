package com.coolightman.demessenger.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.coolightman.demessenger.R
import com.coolightman.demessenger.databinding.FragmentUsersListBinding
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.presentation.adapter.UsersAdapter
import com.coolightman.demessenger.presentation.viewmodel.UsersListViewModel

class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[UsersListViewModel::class.java]
    }

    private lateinit var usersAdapter: UsersAdapter

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity(requireActivity())
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleOnBackPressed()
        createAdapter()
        observers()
        listeners()
    }

    private fun createAdapter() {
        usersAdapter = UsersAdapter { userId -> onUserClick(userId) }
        binding.rvUsers.adapter = usersAdapter
    }

    private fun onUserClick(userId: String) {
        Toast.makeText(requireActivity(), userId, Toast.LENGTH_SHORT).show()
    }

    private fun observers() {
        viewModel.apply {
            isRestartApp.observe(viewLifecycleOwner) {
                if (it) {
                    restartApp()
                }
            }
        }
    }

    private fun listeners() {
        toolbarListener()
    }

    private fun toolbarListener() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    viewModel.logoutUser()
                    true
                }
                else -> false
            }
        }
    }

    private fun restartApp() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}