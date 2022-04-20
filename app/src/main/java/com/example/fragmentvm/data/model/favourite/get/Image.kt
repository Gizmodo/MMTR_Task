package com.example.fragmentvm.data.model.favourite.get

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id") val id: String, // md
    @SerializedName("url") val url: String // https://cdn2.thecatapi.com/images/md.jpg
)