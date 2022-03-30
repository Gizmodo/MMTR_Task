package com.example.fragmentvm.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("appDescription")
    val appDescription: String,
    @SerializedName("email")
    val email: String,
)