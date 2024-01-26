package com.lovevery.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovevery.domain.models.Message
import com.lovevery.domain.usecases.GetAllMessagesUseCase
import com.lovevery.domain.usecases.GetMessagesByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val getMessagesByUserUseCase: GetMessagesByUserUseCase
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    lateinit var currentJob: Job

    fun retrieveAllMessages() {
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val messages = getAllMessagesUseCase()
                Timber.d( "View model messages $messages")
                withContext(Dispatchers.Main) {
                    _messages.value = messages
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching messages")
                withContext(Dispatchers.Main) {
                    _error.value = "Error fetching messages: ${e.message}"
                }
            }
        }
    }

    fun retrieveMessagesByUser(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val messages = getMessagesByUserUseCase(user)
                Timber.d( "View model messages $messages")
                withContext(Dispatchers.Main) {
                    _messages.value = messages
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching messages")
                withContext(Dispatchers.Main) {
                    _error.value = "Error fetching messages: ${e.message}"
                }
            }
        }
    }
}