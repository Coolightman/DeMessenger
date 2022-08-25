package com.coolightman.demessenger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.coolightman.demessenger.databinding.FragmentLoginBinding
import com.coolightman.demessenger.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        listeners()
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

            user.observe(viewLifecycleOwner) {
                if (it != null) {
                    goToUsersListFragment()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.apply {
            btLogin.setOnClickListener {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                viewModel.login(email, password)
            }

            btRegister.setOnClickListener {
                viewModel.resetToast()
                goToRegistrationFragment()
            }

            tvForgotPassword.setOnClickListener {
                val email = binding.etEmail.text.toString().trim()
                viewModel.resetToast()
                goToResetPasswordFragment(email)
            }
        }
    }

    private fun goToRegistrationFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        )
    }

    private fun goToResetPasswordFragment(email: String) {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment(email)
        )
    }

    private fun goToUsersListFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToUsersListFragment()
        )
    }
}