package com.example.fragmentvm.data.model.vote.request

import com.google.gson.annotations.SerializedName

data class VoteRequestDto(
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("value")
    val value: Int,
)
