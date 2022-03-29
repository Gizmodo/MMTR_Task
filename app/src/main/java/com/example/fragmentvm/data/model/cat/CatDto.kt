package com.example.fragmentvm.data.model.cat

import com.google.gson.annotations.SerializedName

data class CatDto(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)
