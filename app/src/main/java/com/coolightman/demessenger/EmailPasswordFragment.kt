package com.coolightman.demessenger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.coolightman.demessenger.databinding.FragmentEmailPasswordBinding

class EmailPasswordFragment : Fragment() {

    companion object {
        const val LOG_TAG = "EmailPasswordFragment"
    }

    private var _binding: FragmentEmailPasswordBinding? = null
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
        _binding = FragmentEmailPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doSomeFirebase()
        listeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.apply {
            tvForgotPassword.setOnClickListener {
                Toast.makeText(ownerActivity, "Go to change password", Toast.LENGTH_SHORT).show()
            }

            btLogin.setOnClickListener {
                Toast.makeText(ownerActivity, "Try to login", Toast.LENGTH_SHORT).show()
            }

            btRegister.setOnClickListener {
                Toast.makeText(ownerActivity, "Go to register", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doSomeFirebase() {
        val userEmail = "drugak48@gmail.com"
        val userPassword = "4815162342"
//        val userEmail = "frinjklan@proton.me"
//        val userPassword = "4815162342"

        addAuthListener()
//        auth.signOut()
//        createUser(userEmail, userPassword)
//        signIn(userEmail, userPassword)
//        resetPassword(userEmail)
//        sendEmailVerification()
    }

    private fun addAuthListener() {
        auth.addAuthStateListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.d(LOG_TAG, "User is NOT authorized")
            } else if (!currentUser.isEmailVerified) {
                Log.d(LOG_TAG, "User email is not verified")
                Toast.makeText(
                    ownerActivity,
                    "Check your e-mail for verification",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d(LOG_TAG, "User is authorized ${currentUser.uid}")
            }
        }
    }

    private fun sendEmailVerification() {
        val currentUser = auth.currentUser
        currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "sendEmailVerification:success")
                Toast.makeText(ownerActivity, "Email verification is sent!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                task.exception?.let {
                    Log.d(LOG_TAG, "sendEmailVerification:failure | ${it.message}")
                    Toast.makeText(
                        ownerActivity,
                        "Can't send email verification",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun resetPassword(userEmail: String) {
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(LOG_TAG, "sendPasswordResetEmail:success")
            } else {
                task.exception?.let {
                    Log.d(LOG_TAG, "sendPasswordResetEmail:failure | ${it.message}")
                    Toast.makeText(ownerActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signIn(userEmail: String, userPassword: String) {
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(ownerActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "signInWithEmailAndPassword:success")
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "signInWithEmailAndPassword:failure | ${it.message}")
                        Toast.makeText(ownerActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun createUser(userEmail: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(ownerActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                } else {
                    task.exception?.let {
                        Log.d(LOG_TAG, "createUserWithEmail:failure | ${it.message}")
                        Toast.makeText(ownerActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
}