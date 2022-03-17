package com.example.fragmentvm.model.login

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("appDescription")
    val appDescription: String,
    @SerializedName("email")
    val email: String,
)