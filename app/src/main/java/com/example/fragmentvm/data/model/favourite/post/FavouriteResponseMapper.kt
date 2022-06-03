package com.example.fragmentvm.data.model.favourite.post

import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavouriteResponseMapper : DomainMapper<FavouriteResponseDto, FavouriteResponseDomain> {
    override fun mapToDomainModel(model: FavouriteResponseDto): FavouriteResponseDomain {
        return FavouriteResponseDomain(
            id = model.id,
            message = model.message,
            adapterPosition = null
        )
    }

    override fun mapFromDomainModel(domainModel: FavouriteResponseDomain): FavouriteResponseDto {
        return FavouriteResponseDto(
            id = domainModel.id ?: 0,
            message = domainModel.message
        )
    }
}
