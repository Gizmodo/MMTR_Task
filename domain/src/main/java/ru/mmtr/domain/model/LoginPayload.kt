package ru.mmtr.domain.model

import com.google.gson.annotations.SerializedName

data class LoginPayload(
    @SerializedName("appDescription")
    val appDescription: String,
    @SerializedName("email")
    val email: String,
)