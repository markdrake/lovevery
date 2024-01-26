package com.lovevery.domain.usecases

import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.models.Message
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetAllMessagesUseCaseImplTest {
    private lateinit var messagesRepository: MessagesRepository
    private lateinit var getAllMessagesUseCase: GetAllMessagesUseCase

    @BeforeEach
    fun setUp() {
        messagesRepository = mock(MessagesRepository::class.java)
        getAllMessagesUseCase = GetAllMessagesUseCaseImpl(messagesRepository)
    }

    @Test
    fun `invoke should return combined messages`() = runBlocking<Unit> {
        // Arrange
        val user1Messages = listOf(Message("user1", "Hello", subject = "Hi"), Message("user1", "Hi", subject = "Hi2"))
        val user2Messages = listOf(Message("user2", "Hey", subject = "Hi3"), Message("user2", "Hola", subject = "hi4"))

        `when`(messagesRepository.getMessages()).thenReturn(
            mapOf("user1" to user1Messages, "user2" to user2Messages)
        )

        // Act
        val result = getAllMessagesUseCase()

        // Assert
        assertEquals(4, result.size)

        // Ensure that messages are combined and have the correct user information
        assertEquals("user1", result[0].user)
        assertEquals("user1", result[1].user)
        assertEquals("user2", result[2].user)
        assertEquals("user2", result[3].user)
    }
}