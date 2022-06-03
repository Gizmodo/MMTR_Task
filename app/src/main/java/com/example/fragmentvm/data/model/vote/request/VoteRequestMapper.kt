package com.example.fragmentvm.data.model.vote.request

import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.utils.DomainMapper
import com.example.fragmentvm.ui.utils.VotesEnum

class VoteRequestMapper : DomainMapper<VoteRequestDto, VoteRequestDomain> {
    override fun mapToDomainModel(model: VoteRequestDto): VoteRequestDomain {
        return VoteRequestDomain(
            imageId = model.imageId,
            value = VotesEnum.getByCode(model.value)
        )
    }

    override fun mapFromDomainModel(domainModel: VoteRequestDomain): VoteRequestDto {
        return VoteRequestDto(
            imageId = domainModel.imageId,
            value = domainModel.value.value
        )
    }
}
