package com.lovevery.ui.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovevery.R
import com.lovevery.domain.models.Message
import com.lovevery.domain.usecases.CreateMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NewMessageViewModel @Inject constructor(
    private val createMessageUseCase: CreateMessageUseCase
) : ViewModel() {

    private val _newMessageForm = MutableLiveData<CreateMessageFormState>()
    val newMessageFormState: LiveData<CreateMessageFormState> = _newMessageForm

    private val _newMessageResult = MutableLiveData<NewMessageResult>()
    val newMessageResult: LiveData<NewMessageResult> = _newMessageResult

    fun saveMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            val status = createMessageUseCase(message)

            withContext(Dispatchers.Main) {
                when (status) {
                    is CreateMessageUseCase.Status.Success -> {
                        _newMessageResult.value = NewMessageResult(success = true)
                    }
                    is CreateMessageUseCase.Status.Error -> {
                        _newMessageResult.value =
                            NewMessageResult(error = R.string.create_message_failed)
                    }
                }
            }

        }
    }

    fun newMessageDataChanged(username: String, subject: String, message: String) {
        when {
            !isUserNameValid(username) -> {
                _newMessageForm.value = CreateMessageFormState(
                    usernameError = R.string.invalid_username
                )
            }

            (!isSubjectValid(subject)) -> {
                _newMessageForm.value = CreateMessageFormState(
                    subjectError = R.string.invalid_subject
                )
            }

            (!isMessageValid(message)) -> {
                _newMessageForm.value = CreateMessageFormState(
                    messageError = R.string.invalid_message
                )
            }

            else -> {
                _newMessageForm.value = CreateMessageFormState(isDataValid = true)
            }
        }
    }

    // Username validation rules
    private fun isUserNameValid(username: String): Boolean =
        username.length in MIN_USERNAME_CHARS..<MAX_USERNAME_CHARS+1

    private fun isSubjectValid(subject: String): Boolean =
        subject.length in MIN_SUBJECT_CHARS ..<MAX_SUBJECT_CHARS+1

    private fun isMessageValid(message: String): Boolean =
        message.length in MIN_MESSAGES_CHARS..<MAX_MESSAGES_CHARS+1

    companion object {
        // Data validation constants, if you adjust these values also adjust the error messages
        private const val MIN_USERNAME_CHARS = 2
        private const val MAX_USERNAME_CHARS = 100
        private const val MIN_SUBJECT_CHARS = 2
        private const val MAX_SUBJECT_CHARS = 100
        private const val MIN_MESSAGES_CHARS = 2
        private const val MAX_MESSAGES_CHARS = 200
    }
}