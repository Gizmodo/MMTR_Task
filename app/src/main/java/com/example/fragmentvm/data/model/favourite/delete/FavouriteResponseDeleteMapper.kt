package com.example.fragmentvm.data.model.favourite.delete

import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDeleteDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavouriteResponseDeleteMapper : DomainMapper<
        FavouriteResponseDeleteDto, FavouriteResponseDeleteDomain> {
    override fun mapToDomainModel(model: FavouriteResponseDeleteDto): FavouriteResponseDeleteDomain {
        return FavouriteResponseDeleteDomain(
            message = model.message
        )
    }

    override fun mapFromDomainModel(domainModel: FavouriteResponseDeleteDomain): FavouriteResponseDeleteDto {
        return FavouriteResponseDeleteDto(
            message = domainModel.message
        )
    }
}
