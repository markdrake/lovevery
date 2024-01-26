package com.lovevery.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateMessageResponseDTO(
    @SerializedName("statusCode")
    val statusCode: String
)