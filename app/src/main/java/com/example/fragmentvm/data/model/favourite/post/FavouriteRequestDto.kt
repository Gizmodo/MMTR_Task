package com.example.fragmentvm.data.model.favourite.post

import com.google.gson.annotations.SerializedName

data class FavouriteRequestDto(
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("sub_id")
    val subId: String,
)
