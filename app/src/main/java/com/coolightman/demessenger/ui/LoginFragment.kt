package com.coolightman.demessenger.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.demessenger.databinding.FragmentLoginBinding
import com.coolightman.demessenger.utils.isEmailValid
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authentication()
        listeners()
    }

    private fun authentication() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(LOG_TAG, "User is NOT authorized")
        } else checkEmailVerifyAndEnter(currentUser)
    }

    private fun checkEmailVerifyAndEnter(currentUser: FirebaseUser) {
        if (!currentUser.isEmailVerified) {
            Log.d(LOG_TAG, "User email is not verified")
            Toast.makeText(
                ownerActivity,
                "Check your e-mail for verification ",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.d(LOG_TAG, "User is authorized ${currentUser.uid}")
            goToUsersListFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.apply {
            tvForgotPassword.setOnClickListener {
                val email = binding.etEmail.text.toString().trim()
                goToResetPasswordFragment(email)
            }

            btLogin.setOnClickListener {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                if (isNotEmptyFields(password, email)) {
                    if (isEmailValid(email)) {
                        signInFirebase(email, password)
                    } else {
                        Toast.makeText(ownerActivity, "E-mail is not valid", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(ownerActivity, "Some fields are empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            btRegister.setOnClickListener {
                goToRegistrationFragment()
            }
        }
    }

    private fun isNotEmptyFields(password: String, email: String) =
        password.isNotEmpty() && email.isNotEmpty()

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

    private fun signInFirebase(userEmail: String, userPassword: String) {
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(ownerActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "signInWithEmailAndPassword:success")
                    checkEmailVerifyAndEnter(auth.currentUser!!)
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "signInWithEmailAndPassword:failure | ${it.message}")
                        Toast.makeText(ownerActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    companion object {
        const val LOG_TAG = "EmailPasswordFragment"
    }
}