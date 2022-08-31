package com.coolightman.demessenger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private var _isSuccessRegistration = MutableLiveData<Boolean>()
    val isSuccessRegistration: LiveData<Boolean>
        get() = _isSuccessRegistration

    fun registerUser(nickname: String, email: String, password: String) {
        viewModelScope.launch {
            if (isNotEmptyFields(nickname, email, password)) {
                registerUserUseCase(nickname, email, password).collect { result ->
                    when (result) {
                        is ResultOf.Success -> {
                            _toast.postValue("Your account created successfully")
                            _isSuccessRegistration.postValue(true)
                        }
                        is ResultOf.Error -> {
                            _toastLong.postValue(result.message)
                        }
                    }
                }
            } else {
                _toast.postValue("Some fields are empty")
            }
        }
    }

    private fun isNotEmptyFields(nickname: String, email: String, password: String) =
        nickname.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()

    companion object {
        private const val LOG_TAG = "RegistrationViewModel"
    }
}