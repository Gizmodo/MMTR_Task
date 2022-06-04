package com.example.fragmentvm.data.model.favourite.get

import com.example.fragmentvm.domain.model.favourite.FavCatListDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class FavCatListMapper : DomainMapper<FavCatListDto, FavCatListDomain> {
    override fun mapToDomainModel(model: FavCatListDto): FavCatListDomain {
        val data = FavCatListDomain()
        model.forEach {
            data.add(FavCatMapper().mapToDomainModel(it))
        }
        return data
    }

    override fun mapFromDomainModel(domainModel: FavCatListDomain): FavCatListDto {
        val data = FavCatListDto()
        domainModel.forEach {
            data.add(FavCatMapper().mapFromDomainModel(it))
        }
        return data
    }
}
