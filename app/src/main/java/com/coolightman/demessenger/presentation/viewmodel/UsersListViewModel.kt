package com.coolightman.demessenger.presentation.viewmodel

import androidx.lifecycle.*
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.usecase.GetAllUsersUseCase
import com.coolightman.demessenger.domain.usecase.LogoutUserUseCase
import com.coolightman.demessenger.domain.usecase.SetUserIsOnlineUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val setUserIsOnlineUseCase: SetUserIsOnlineUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val userId by lazy {
        state.get<String>("userId")!!
    }

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>>
        get() = _usersList

    private val _isRestartApp = MutableLiveData<Boolean>()
    val isRestartApp: LiveData<Boolean>
        get() = _isRestartApp

    init {
        loadUsersList()
    }

    private lateinit var getAllUsersJob: Job

    fun setUserIsOnline(isOnline: Boolean) {
        viewModelScope.launch {
            setUserIsOnlineUseCase(userId, isOnline)
        }
    }

    private fun loadUsersList() {
        getAllUsersJob = viewModelScope.launch {
            getAllUsersUseCase(userId).collect { result ->
                when (result) {
                    is ResultOf.Success -> {
                        _usersList.postValue(result.value)
                    }
                    is ResultOf.Error -> {
                        _toastLong.postValue(result.message)
                    }
                }
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            val logoutJob = launch {
                getAllUsersJob.cancel()
                setUserIsOnline(false)
                logoutUserUseCase()
                delay(SIGN_OUT_PAUSE)
            }
            logoutJob.join()
            restartApplication()
        }
    }

    private fun restartApplication() {
        _isRestartApp.postValue(true)
    }

    fun resetToast() {
        _toast.postValue("")
        _toastLong.postValue("")
    }

    companion object {
        private const val SIGN_OUT_PAUSE = 500L
    }
}