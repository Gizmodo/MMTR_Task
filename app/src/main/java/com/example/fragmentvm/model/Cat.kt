package com.example.fragmentvm.model

data class Cat(
    val breeds: List<Any>,
    var height: Int,
    val id: String,
    val url: String,
    val width: Int,
    val original_filename: String?,
    var state: Boolean = true
)