package com.coolightman.demessenger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private var _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private var _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private var _isSuccessReset = MutableLiveData<Boolean>()
    val isSuccessReset: LiveData<Boolean>
        get() = _isSuccessReset

    fun resetPassword(userEmail: String) {
        if (userEmail.isNotEmpty()) {
            resetPasswordFirebase(userEmail)
        } else {
            _toast.postValue("Some fields are empty")
        }
    }

    private fun resetPasswordFirebase(userEmail: String) {
        viewModelScope.launch {
            resetPasswordUseCase(userEmail).collect { result ->
                when (result) {
                    is ResultOf.Success -> {
                        _toast.postValue("Reset password email is sent")
                        _isSuccessReset.postValue(true)
                    }
                    is ResultOf.Error -> {
                        _toastLong.postValue(result.message)
                    }
                }
            }
        }
    }
}