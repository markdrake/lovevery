package com.lovevery.domain.usecases

import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.models.Message
import javax.inject.Inject

interface CreateMessageUseCase {
    suspend operator fun invoke(message: Message): Status

    sealed class Status {
        data object Success : Status()
        data class Error(val code: Int) : Status()
    }
}

class CreateMessageUseCaseImpl @Inject constructor(
    private val messagesRepository: MessagesRepository
): CreateMessageUseCase {
    override suspend operator fun invoke(message: Message): CreateMessageUseCase.Status {
        val result = messagesRepository.createMessage(message)
        return when {
            result.isSuccessful -> {
                CreateMessageUseCase.Status.Success
            }
            else -> CreateMessageUseCase.Status.Error(result.code())
        }
    }
}