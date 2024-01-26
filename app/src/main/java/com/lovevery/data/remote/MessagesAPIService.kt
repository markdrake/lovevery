package com.lovevery.data.remote

import com.lovevery.data.remote.models.CreateMessageRequestDTO
import com.lovevery.data.remote.models.CreateMessageResponseDTO
import com.lovevery.data.remote.models.GetMessagesResponseDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessagesAPIService {
    @GET("/proto/messages")
    fun getMessages(): Call<GetMessagesResponseDTO>

    @GET("/proto/messages/{$USERNAME_PATH}")
    fun getMessagesByUser(@Path(USERNAME_PATH) user: String): Call<GetMessagesResponseDTO>

    @POST("/proto/messages")
    suspend fun createMessage(@Body request: CreateMessageRequestDTO): Response<CreateMessageResponseDTO>

    companion object {
        private const val USERNAME_PATH = "username"
    }
}