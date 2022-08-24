package com.coolightman.demessenger.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.demessenger.databinding.FragmentRegistrationBinding
import com.coolightman.demessenger.utils.isEmailValid


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
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
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        binding.apply {
            btRegister.setOnClickListener {
                val nickname = etNickname.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (isNotEmptyFields(nickname, email, password)) {
                    if (isEmailValid(email)) {
                        createUser(email, password)
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

    private fun isNotEmptyFields(nickname: String, email: String, password: String) =
        nickname.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()


    private fun createUser(userEmail: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(ownerActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                    sendEmailVerification()
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "createUserWithEmail:failure | ${it.message}")
                        Toast.makeText(ownerActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun sendEmailVerification() {
        val currentUser = auth.currentUser
        currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "sendEmailVerification:success")
                findNavController().popBackStack()
            } else {
                task.exception?.let {
                    Log.d(LOG_TAG, "sendEmailVerification:failure | ${it.message}")
                    Toast.makeText(ownerActivity, it.message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LOG_TAG = "RegistrationFragment"
    }
}