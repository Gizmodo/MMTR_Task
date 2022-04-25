package com.example.fragmentvm.domain.model.favourite

data class FavCatDomain(
    val createdAt: String,
    val id: Int,
    val image_url: String?,
    val imageId: String,
    val subId: String,
    val userId: String,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,
)