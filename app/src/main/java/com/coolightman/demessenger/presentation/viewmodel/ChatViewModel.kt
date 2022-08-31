package com.coolightman.demessenger.presentation.viewmodel

import androidx.lifecycle.*
import com.coolightman.demessenger.domain.entity.Message
import com.coolightman.demessenger.domain.entity.ResultOf
import com.coolightman.demessenger.domain.entity.User
import com.coolightman.demessenger.domain.usecase.GetChatMessagesUseCase
import com.coolightman.demessenger.domain.usecase.GetUserUseCase
import com.coolightman.demessenger.domain.usecase.SendMessageUseCase
import com.coolightman.demessenger.domain.usecase.SetUserIsOnlineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val setUserIsOnlineUseCase: SetUserIsOnlineUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val userId by lazy {
        state.get<String>("userId")!!
    }
    private val companionId by lazy {
        state.get<String>("companionId")!!
    }

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastLong = MutableLiveData<String>()
    val toastLong: LiveData<String>
        get() = _toastLong

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>>
        get() = _messages

    private val _companion = MutableLiveData<User>()
    val companion: LiveData<User>
        get() = _companion

    private val _isSentMessage = MutableLiveData<Boolean>()
    val isSentMessage: LiveData<Boolean>
        get() = _isSentMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getCompanionData()
        fetchMessages()
    }

    fun setUserIsOnline(isOnline: Boolean) {
        viewModelScope.launch {
            setUserIsOnlineUseCase(userId, isOnline)
        }
    }

    private fun fetchMessages() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            getChatMessagesUseCase(userId, companionId).collect { result ->
                when (result) {
                    is ResultOf.Success -> {
                        _isLoading.postValue(false)
                        _messages.postValue(result.value)
                    }
                    is ResultOf.Error -> {
                        _isLoading.postValue(false)
                        _toastLong.postValue(result.message)
                    }
                }
            }
        }
    }

    private fun getCompanionData() {
        viewModelScope.launch {
            getUserUseCase(companionId).collect { result ->
                when (result) {
                    is ResultOf.Success -> {
                        _companion.postValue(result.value)
                    }
                    is ResultOf.Error -> {
                        _toastLong.postValue(result.message)
                    }
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val message = createMessage(text)
            sendMessageUseCase(message).collect { result ->
                when (result) {
                    is ResultOf.Success -> {
                        _isSentMessage.postValue(true)
                    }
                    is ResultOf.Error -> {
                        _toastLong.postValue(result.message)
                    }
                }
            }
        }
    }

    private fun createMessage(text: String): Message =
        Message(text, userId, companionId)

}