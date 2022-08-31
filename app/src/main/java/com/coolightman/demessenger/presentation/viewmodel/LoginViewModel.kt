package com.coolightman.demessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.usecase.GetFirebaseUserUseCase
import com.coolightman.demessenger.domain.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getFirebaseUserUseCase: GetFirebaseUserUseCase,
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?>
        get() = _userId

    init {
        authentication()
    }

    private fun authentication() {
        viewModelScope.launch {
            val currentUser = getFirebaseUserUseCase()
            if (currentUser != null) {
                _userId.postValue(currentUser.uid)
            } else {
                Log.d(LOG_TAG, "User is NOT authorized")
                _userId.postValue(null)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (isNotEmptyFields(password, email)) {
                loginUserUseCase(email, password).collect { result ->
                    when (result) {
                        is ResultOf.Success -> {
                            authentication()
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

    private fun isNotEmptyFields(password: String, email: String) =
        password.isNotEmpty() && email.isNotEmpty()

    fun resetToast() {
        _toast.postValue("")
        _toastLong.postValue("")
    }

    companion object {
        private const val LOG_TAG = "LoginViewModel"
    }

}