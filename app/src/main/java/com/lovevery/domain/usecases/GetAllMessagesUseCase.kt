package com.lovevery.domain.usecases

import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.models.Message
import javax.inject.Inject

interface GetAllMessagesUseCase {
    suspend operator fun invoke(): List<Message>
}

class GetAllMessagesUseCaseImpl @Inject constructor(
    private val messagesRepository: MessagesRepository
): GetAllMessagesUseCase {
    override suspend operator fun invoke(): List<Message> {
        val messages = messagesRepository.getMessages()
        val result: MutableList<Message> = mutableListOf()
        messages.forEach { (user, userMessages) ->
            result.addAll(userMessages.map {
                it.copy(user = user)
            })
        }

        return result
    }
}