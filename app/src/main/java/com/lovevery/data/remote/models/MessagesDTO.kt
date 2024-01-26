package com.lovevery.data.remote.models

import com.google.gson.annotations.SerializedName

data class MessagesDTO(
    @SerializedName("user")
    val user: String?,
    @SerializedName("subject")
    val subject: String?,
    @SerializedName("message")
    val message: String?
)

data class UserMessageResponseDTO(
    @SerializedName("user")
    val user: String,
    @SerializedName("message")
    val message: List<NestedMessageResponseDTO>
)

data class NestedMessageResponseDTO(
    @SerializedName("subject")
    val subject: String,
    @SerializedName("message")
    val message: String
)