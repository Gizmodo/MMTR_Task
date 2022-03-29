package com.example.fragmentvm.domain.model.login

data class BackendResponseDomain(
    val message: String,
    val status: Int?,
    val level: String?,
)