package com.lovevery.domain.usecases

import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.models.Message
import javax.inject.Inject

interface GetMessagesByUserUseCase {
    suspend operator fun invoke(user: String): List<Message>
}

class GetMessagesByUserUseCaseImpl @Inject constructor(
    private val messagesRepository: MessagesRepository
): GetMessagesByUserUseCase {
    override suspend operator fun invoke(user: String): List<Message> =
        messagesRepository.getMessagesByUser(user)
}