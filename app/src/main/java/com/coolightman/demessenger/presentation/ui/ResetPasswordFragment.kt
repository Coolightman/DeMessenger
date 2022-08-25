package com.coolightman.demessenger.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.coolightman.demessenger.databinding.FragmentResetPasswordBinding
import com.coolightman.demessenger.presentation.viewmodel.ResetPasswordViewModel

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[ResetPasswordViewModel::class.java]
    }

    private val args by navArgs<ResetPasswordFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = args.email
        setEmailInEditText(email)
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
            isSuccessReset.observe(viewLifecycleOwner) {
                if (it) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setEmailInEditText(email: String) {
        binding.etEmail.setText(email)
    }

    private fun listeners() {
        binding.apply {
            btResetPassword.setOnClickListener {
                val email = etEmail.text.toString().trim()
                viewModel.resetPassword(email)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}