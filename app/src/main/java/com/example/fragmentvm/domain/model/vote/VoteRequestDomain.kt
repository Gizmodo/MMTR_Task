package com.example.fragmentvm.domain.model.vote

import com.example.fragmentvm.ui.utils.VotesEnum

data class VoteRequestDomain(
    val imageId: String,
    val value: VotesEnum,
)