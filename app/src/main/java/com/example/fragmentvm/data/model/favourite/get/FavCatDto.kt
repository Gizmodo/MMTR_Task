package com.example.fragmentvm.data.model.favourite.get

import com.google.gson.annotations.SerializedName

data class FavCatDto(
    @SerializedName("created_at") val createdAt: String, // 2022-04-19T11:20:13.000Z
    @SerializedName("id") val id: Int, // 2152330
    @SerializedName("image") val image: Image?,
    @SerializedName("image_id") val imageId: String, // md
    @SerializedName("sub_id") val subId: String, // demo-440b14
    @SerializedName("user_id") val userId: String, // dxjh4r
)