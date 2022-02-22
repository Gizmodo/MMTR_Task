package com.example.fragmentvm.model

data class BackendResponse(
    val level: String,
    val message: String,
    val status: Int,
    var position: Int,
    var vote: VotesEnum = VotesEnum.UNDEFINED,
)