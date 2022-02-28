package com.example.fragmentvm.model.cat

import com.google.gson.annotations.SerializedName

data class CatModel(
    val breeds: List<Any>,
    var height: Int,
    val id: String,
    val url: String,
    val width: Int,
    @SerializedName("original_filename")
    val originalFilename: String?,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,
)