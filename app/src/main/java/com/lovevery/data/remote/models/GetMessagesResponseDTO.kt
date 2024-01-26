package com.lovevery.data.remote.models

import com.google.gson.annotations.SerializedName

data class GetMessagesResponseDTO(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("body")
    val body: String?
)