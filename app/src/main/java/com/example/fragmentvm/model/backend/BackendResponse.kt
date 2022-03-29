package com.example.fragmentvm.model.backend

import com.example.fragmentvm.ui.utils.VotesEnum

data class BackendResponse(
    val level: String,
    val message: String,
    val status: Int,
    var position: Int,
    var vote: VotesEnum = VotesEnum.UNDEFINED,
)