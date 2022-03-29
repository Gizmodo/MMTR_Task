package com.example.fragmentvm.data.model.vote

import com.example.fragmentvm.domain.model.VoteDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class VoteDtoMapper : DomainMapper<VoteDto, VoteDomain> {
    override fun mapToDomainModel(model: VoteDto): VoteDomain {
        return VoteDomain(
            imageId = model.imageId,
            value = model.value
        )
    }

    override fun mapFromDomainModel(domainModel: VoteDomain): VoteDto {
        return VoteDto(
            imageId = domainModel.imageId,
            value = domainModel.value
        )
    }
}