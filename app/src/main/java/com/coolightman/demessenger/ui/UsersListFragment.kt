package com.coolightman.demessenger.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.coolightman.demessenger.R
import com.coolightman.demessenger.databinding.FragmentUsersListBinding
import kotlinx.coroutines.delay

class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private val auth by lazy {
        (requireActivity() as MainActivity).auth
    }

    private val ownerActivity by lazy {
        requireActivity()
    }

    private fun handleOnBackPressed() {
        ownerActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity(ownerActivity)
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
        listeners()
    }

    private fun listeners() {
        toolbarListener()
    }

    private fun toolbarListener() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    lifecycleScope.launchWhenStarted {
                        auth.signOut()
                        delay(500L)
                        restartApp()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun restartApp() {
        val intent = Intent(ownerActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LOG_TAG = "UsersListFragment"
    }
}