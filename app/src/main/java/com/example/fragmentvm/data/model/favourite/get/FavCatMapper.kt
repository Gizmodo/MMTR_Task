package com.example.fragmentvm.data.model.favourite.get

import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavCatMapper : DomainMapper<
        FavCatDto,
        FavCatDomain
        > {
    override fun mapToDomainModel(model: FavCatDto): FavCatDomain {
        return FavCatDomain(
            createdAt = model.createdAt,
            id = model.id,
            image_url = model.image?.url,
            imageId = model.imageId,
            subId = model.subId,
            userId = model.userId,
        )
    }

    override fun mapFromDomainModel(domainModel: FavCatDomain): FavCatDto {
        return FavCatDto(
            createdAt = domainModel.createdAt,
            id = domainModel.id,
            image = null,
            imageId = domainModel.imageId,
            subId = domainModel.subId,
            userId = domainModel.userId,
        )
    }

    fun toDomainList(initial: List<FavCatDto>): List<FavCatDomain> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<FavCatDomain>): List<FavCatDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}
