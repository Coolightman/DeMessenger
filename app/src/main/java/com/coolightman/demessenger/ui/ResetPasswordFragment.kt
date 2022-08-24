package com.coolightman.demessenger.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.demessenger.databinding.FragmentResetPasswordBinding
import com.coolightman.demessenger.utils.isEmailValid

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val auth by lazy {
        (requireActivity() as MainActivity).auth
    }

    private val ownerActivity by lazy {
        requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        binding.apply {
            btResetPassword.setOnClickListener {
                val email = etEmail.text.toString().trim()
                if (email.isNotEmpty()) {
                    if (isEmailValid(email)) {
                        resetPassword(email)
                    } else {
                        Toast.makeText(ownerActivity, "E-mail is not valid", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(ownerActivity, "Some fields are empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun resetPassword(userEmail: String) {
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "sendPasswordResetEmail:success")
                Toast.makeText(ownerActivity, "Reset password email is sent", Toast.LENGTH_SHORT)
                    .show()
                goBack()
            } else {
                task.exception?.let {
                    Log.d(LOG_TAG, "sendPasswordResetEmail:failure | ${it.message}")
                    Toast.makeText(ownerActivity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LOG_TAG = "ResetPasswordFragment"
    }
}