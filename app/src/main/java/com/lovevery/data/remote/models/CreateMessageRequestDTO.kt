package com.lovevery.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateMessageRequestDTO(
    @SerializedName("user")
    val user: String?,
    @SerializedName("operation")
    val operation: String = DEFAULT_OPERATION,
    @SerializedName("subject")
    val subject: String?,
    @SerializedName("message")
    val message: String?
) {
    companion object {
        private const val DEFAULT_OPERATION = "add_message"
    }
}