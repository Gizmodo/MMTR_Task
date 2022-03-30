package com.example.fragmentvm.data.model.cat

import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class CatDtoMapper : DomainMapper<CatDto, CatDomain> {
    override fun mapToDomainModel(model: CatDto): CatDomain {
        return CatDomain(
            id = model.id,
            url = model.url,
            width = model.width,
            height = model.height,
            isLiked = false,
            isDisliked = false
        )
    }

    override fun mapFromDomainModel(domainModel: CatDomain): CatDto {
        return CatDto(
            id = domainModel.id,
            url = domainModel.url,
            width = domainModel.width,
            height = domainModel.height
        )
    }

    fun toDomainList(initial: List<CatDto>): List<CatDomain> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<CatDomain>): List<CatDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}