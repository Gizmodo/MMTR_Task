package com.example.fragmentvm.data.model.favourite.post

import com.example.fragmentvm.domain.model.favourite.FavouriteRequestDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavouriteRequestMapper : DomainMapper<FavouriteRequestDto, FavouriteRequestDomain> {
    override fun mapToDomainModel(model: FavouriteRequestDto): FavouriteRequestDomain {
        return FavouriteRequestDomain(
            imageId = model.imageId,
            subId = model.subId
        )
    }

    override fun mapFromDomainModel(domainModel: FavouriteRequestDomain): FavouriteRequestDto {
        return FavouriteRequestDto(
            imageId = domainModel.imageId,
            subId = domainModel.subId
        )
    }
}
