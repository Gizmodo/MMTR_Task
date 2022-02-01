package com.example.fragmentvm.model

import com.google.gson.annotations.SerializedName

data class VotePayload(
    @SerializedName("image_id")
    val image_id: String,
    @SerializedName("value")
    val value: Int,
)
