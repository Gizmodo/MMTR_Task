package com.example.fragmentvm.domain.model.vote

import com.example.fragmentvm.ui.utils.VotesEnum

data class VoteResponseDomain(
    val id: Int = -1,
    var message: String,
    var position: Int,
    var vote: VotesEnum,
)
