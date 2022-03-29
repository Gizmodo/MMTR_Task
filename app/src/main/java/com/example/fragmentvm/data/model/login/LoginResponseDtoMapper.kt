package com.example.fragmentvm.data.model.login

import com.example.fragmentvm.domain.model.login.LoginResponseDomain
import com.example.fragmentvm.domain.utils.DomainMapper

class LoginResponseDtoMapper : DomainMapper<LoginResponseDto, LoginResponseDomain> {
    override fun mapToDomainModel(model: LoginResponseDto): LoginResponseDomain {
        return LoginResponseDomain(
            message = model.message,
            status = model.status,
            level = model.level
        )
    }

    override fun mapFromDomainModel(domainModel: LoginResponseDomain): LoginResponseDto {
        return LoginResponseDto(
            message = domainModel.message,
            status = domainModel.status,
            level = domainModel.level
        )
    }
}