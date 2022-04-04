package com.example.fragmentvm.temp

import com.google.gson.annotations.SerializedName

data class CatResponseItem(
    @SerializedName("breeds") val breeds: List<Any>,
    @SerializedName("categories") val categories: List<Category>,
    @SerializedName("height") val height: Int, // 280
    @SerializedName("id") val id: String, // ouB9E19qB
    @SerializedName("url") val url: String, // https://cdn2.thecatapi.com/images/ouB9E19qB.jpg
    @SerializedName("width") val width: Int, // 347
)