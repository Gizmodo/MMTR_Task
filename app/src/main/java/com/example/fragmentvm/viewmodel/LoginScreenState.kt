package com.example.fragmentvm.viewmodel

sealed class LoginScreenState {
    object Initial : LoginScreenState()
    data class LoginSuccess(val response: LoginResponse) : LoginScreenState()
    data class LoginFailure(val error: String) : LoginScreenState()
    object Loading : LoginScreenState()
    data class EmailValidationError(val error: String) : LoginScreenState()
    data class PasswordValidationError(val error: String) : LoginScreenState()
}