package com.example.fragmentvm.data.model.vote

import com.google.gson.annotations.SerializedName

data class VoteDto(
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("value")
    val value: Int,
)
