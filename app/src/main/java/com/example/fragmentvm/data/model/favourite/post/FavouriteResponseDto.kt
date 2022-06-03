package com.example.fragmentvm.data.model.favourite.post

import com.google.gson.annotations.SerializedName

data class FavouriteResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
)
