package com.example.fragmentvm.data.model.favourite.post

import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavoriteResponseMapper : DomainMapper<FavouriteResponseDto, FavouriteResponseDomain> {
    override fun mapToDomainModel(model: FavouriteResponseDto): FavouriteResponseDomain {
        return FavouriteResponseDomain(
            id = model.id,
            message = model.message
        )
    }

    override fun mapFromDomainModel(domainModel: FavouriteResponseDomain): FavouriteResponseDto {
        return FavouriteResponseDto(
            id = domainModel.id,
            message = domainModel.message
        )
    }
}