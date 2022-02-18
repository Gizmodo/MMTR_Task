package ru.mmtr.domain.model

import ru.mmtr.domain.utils.VotesEnum

data class BackendResponse(
    val level: String,
    val message: String,
    val status: Int,
    var position: Int,
    var vote: VotesEnum = VotesEnum.UNDEFINED,
)