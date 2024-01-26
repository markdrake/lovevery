package com.lovevery.domain.usecases

import com.lovevery.data.remote.models.CreateMessageResponseDTO
import com.lovevery.data.repositories.MessagesRepository
import com.lovevery.domain.models.Message
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class CreateMessageUseCaseImplTest {
    private lateinit var messagesRepository: MessagesRepository
    private lateinit var createMessageUseCase: CreateMessageUseCase


    @BeforeEach
    fun setUp() {
        messagesRepository = mock(MessagesRepository::class.java)
        createMessageUseCase = CreateMessageUseCaseImpl(messagesRepository)
    }

    @Test
    fun `invoke should return Success status on successful message creation`() = runBlocking<Unit>{
        // Arrange
        val message = Message("mark", "Hello", "Subject")
        `when`(messagesRepository.createMessage(message)).thenReturn(
            Response.success(CreateMessageResponseDTO("Success"))
        )

        // Act
        val result = createMessageUseCase(message)

        // Assert
        assertEquals(CreateMessageUseCase.Status.Success, result)
    }

    @Test
    fun `invoke should return Error status on unsuccessful message creation`() = runBlocking<Unit> {
        // Arrange
        val message = Message("user1", "Hello", "Subject")
        val expectedErrorCode = 500
        `when`(messagesRepository.createMessage(message)).thenReturn(
            Response.error(expectedErrorCode,
                "{body: \"failed to create message\""
                    .toResponseBody("application/json".toMediaTypeOrNull())
                )
        )

        // Act
        val result = createMessageUseCase(message)

        // Assert
        assertEquals(CreateMessageUseCase.Status.Error(expectedErrorCode), result)
    }
}