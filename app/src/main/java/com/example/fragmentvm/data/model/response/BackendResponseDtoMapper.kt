package com.example.fragmentvm.data.model.response

import com.example.fragmentvm.domain.model.BackendResponseDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class BackendResponseDtoMapper : DomainMapper<BackendResponseDto, BackendResponseDomain> {
    override fun mapToDomainModel(model: BackendResponseDto): BackendResponseDomain {
        return BackendResponseDomain(
            message = model.message,
            status = model.status,
            level = model.level
        )
    }

    override fun mapFromDomainModel(domainModel: BackendResponseDomain): BackendResponseDto {
        return BackendResponseDto(
            message = domainModel.message,
            status = domainModel.status,
            level = domainModel.level
        )
    }

    fun toDomainList(initial: List<BackendResponseDto>): List<BackendResponseDomain> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<BackendResponseDomain>): List<BackendResponseDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}
