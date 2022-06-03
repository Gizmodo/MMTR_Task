package com.example.fragmentvm.data.model.response

import com.google.gson.annotations.SerializedName

data class BackendResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("level")
    val level: String?,
)
