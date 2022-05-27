package com.example.fragmentvm.data.model.vote.response

import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.domain.utils.DomainMapper
import com.example.fragmentvm.ui.utils.VotesEnum
import retrofit2.Response

class VoteResponseMapper : DomainMapper<VoteResponseDto, VoteResponseDomain> {
    override fun mapToDomainModel(model: VoteResponseDto): VoteResponseDomain {
        return VoteResponseDomain(
            id = model.id,
            message = model.message,
            position = -1,
            vote = VotesEnum.UNDEFINED
        )
    }

    override fun mapFromDomainModel(domainModel: VoteResponseDomain): VoteResponseDto {
        return VoteResponseDto(
            id = domainModel.id,
            message = domainModel.message
        )
    }

    fun convertResponseToDomain(model: Response<VoteResponseDto>): Response<VoteResponseDomain> {
        val result: Response<VoteResponseDomain>
        mapToDomainModel(model.body()).also { result.body = it }
        return mapToDomainModel(model)
    }
}