package com.example.fragmentvm.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int?,
    @SerializedName("level") val level: String?,
)