package com.example.fragmentvm.data.model.favourite.get

data class FavouritesListItem(
    val created_at: String,
    val id: Int,
    val image: Image,
    val image_id: String,
    val sub_id: String,
    val user_id: String
)