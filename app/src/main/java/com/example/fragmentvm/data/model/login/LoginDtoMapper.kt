package com.example.fragmentvm.data.model.login

import com.example.fragmentvm.domain.model.login.LoginDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class LoginDtoMapper : DomainMapper<LoginDto, LoginDomain> {

    override fun mapToDomainModel(model: LoginDto): LoginDomain {
        return LoginDomain(
            appDescription = model.appDescription,
            email = model.email
        )
    }

    override fun mapFromDomainModel(domainModel: LoginDomain): LoginDto {
        return LoginDto(
            email = domainModel.email,
            appDescription = domainModel.appDescription
        )
    }

    fun toDomainList(initial: List<LoginDto>): List<LoginDomain> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<LoginDomain>): List<LoginDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}