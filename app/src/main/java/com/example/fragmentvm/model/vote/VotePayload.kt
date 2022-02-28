package com.example.fragmentvm.model.vote

import com.google.gson.annotations.SerializedName

data class VotePayload(
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("value")
    val value: Int,
)
