package com.example.fragmentvm.data.model.vote.response

import com.google.gson.annotations.SerializedName

data class VoteResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
)
