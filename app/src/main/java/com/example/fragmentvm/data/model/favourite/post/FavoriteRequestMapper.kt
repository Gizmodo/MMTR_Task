package com.example.fragmentvm.data.model.favourite.post

import com.example.fragmentvm.domain.model.favourite.FavouriteRequestDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavoriteRequestMapper : DomainMapper<FavoriteRequestDto, FavouriteRequestDomain> {
    override fun mapToDomainModel(model: FavoriteRequestDto): FavouriteRequestDomain {
        return FavouriteRequestDomain(
            imageId = model.imageId,
            subId = model.subId
        )
    }

    override fun mapFromDomainModel(domainModel: FavouriteRequestDomain): FavoriteRequestDto {
        return FavoriteRequestDto(
            imageId = domainModel.imageId,
            subId = domainModel.subId
        )
    }
}