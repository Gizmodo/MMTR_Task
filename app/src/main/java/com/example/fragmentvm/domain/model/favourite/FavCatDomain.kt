package com.example.fragmentvm.domain.model.favourite

data class FavCatDomain(
    val createdAt: String, // 2022-04-19T11:20:13.000Z
    val id: Int, // 2152330
    val image_url: String?,
    val imageId: String, // md
    val subId: String, // demo-440b14
    val userId: String, // dxjh4r
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,
)