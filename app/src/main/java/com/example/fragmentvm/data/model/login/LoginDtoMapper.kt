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
}
