package com.example.fragmentvm.temp


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: Int, // 15
    @SerializedName("name") val name: String // clothes
)