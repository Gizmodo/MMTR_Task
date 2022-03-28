package com.example.fragmentvm.domain.model

data class CatDomain(
    val id: String,
    val url: String,
    val width: Int,
    var height: Int,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,
)