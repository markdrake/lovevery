package com.lovevery.data.repositories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lovevery.data.remote.MessagesAPIService
import com.lovevery.data.remote.models.CreateMessageRequestDTO
import com.lovevery.data.remote.models.CreateMessageResponseDTO
import com.lovevery.data.remote.models.UserMessageResponseDTO
import com.lovevery.domain.models.Message
import retrofit2.Response
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val messagesAPIService: MessagesAPIService
) {

    suspend fun getMessages(): Map<String, List<Message>> {
        val response = messagesAPIService.getMessages().execute().body()
        var result: Map<String, MutableList<Message>> = mutableMapOf()

        response?.let {
            if (it.statusCode != RESOURCE_NOT_FOUND && it.body != EMPTY_OBJECT) {
                // The type is needed because of the nested generics
                val type = object : TypeToken<HashMap<String, List<Message>>>() {}.type
                result = Gson().fromJson(it.body, type)
            }
        }

        return result
    }

    suspend fun getMessagesByUser(user: String) : List<Message> {
        val response = messagesAPIService.getMessagesByUser(user).execute().body()
        val result: MutableList<Message> = mutableListOf()

        response?.let {responseDto ->
            if (responseDto.statusCode != RESOURCE_NOT_FOUND && responseDto.body != EMPTY_OBJECT) {

                val parsed = Gson().fromJson(responseDto.body, UserMessageResponseDTO::class.java)
                result.addAll(
                    parsed.message.map {
                        Message(
                            user = parsed.user,
                            message = it.message,
                            subject = it.subject
                        )
                    }
                )
            }
        }

        return result
    }

    suspend fun createMessage(message: Message): Response<CreateMessageResponseDTO> =
        messagesAPIService.createMessage(
            CreateMessageRequestDTO(
                user = message.user,
                subject = message.subject,
                message = message.message
            )
        )

    companion object {
        private const val EMPTY_OBJECT = "{}"
        private const val RESOURCE_NOT_FOUND = 404
    }
}