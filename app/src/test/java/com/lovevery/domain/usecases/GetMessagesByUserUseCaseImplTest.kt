package com.lovevery.domain.usecases

import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.models.Message
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetMessagesByUserUseCaseImplTest {
    private lateinit var messagesRepository: MessagesRepository
    private lateinit var getMessagesByUseCase: GetMessagesByUserUseCase

    @BeforeEach
    fun setUp() {
        messagesRepository = mock(MessagesRepository::class.java)
        getMessagesByUseCase = GetMessagesByUserUseCaseImpl(messagesRepository)
    }

    @Test
    fun `invoke should return combined messages`() = runBlocking<Unit> {
        // Arrange
        val user = "Mark"
        val messages = listOf(Message(user, "Hello", subject = "Hi"), Message(user, "Hi", subject = "Hi2"))

        `when`(messagesRepository.getMessagesByUser(user)).thenReturn(
           messages
        )

        // Act
        val result = getMessagesByUseCase(user)

        // Assert
        assertEquals(2, result.size)

        // Ensure that messages are combined and have the correct user information
        assertEquals(user, result[0].user)
        assertEquals(user, result[1].user)
    }
}