package ru.mmtr.domain.model

import com.google.gson.annotations.SerializedName

data class Cat(
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