package com.example.fragmentvm.core.utils

import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.domain.utils.DomainMapper
import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}

suspend fun <T : Any, DM : Any> api(
    mapper: DomainMapper<T, DM>,
    execute: suspend () -> Response<T>,
): NetworkResult<DM> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            val res: DM = mapper.mapToDomainModel(body)
            NetworkResult.Success(res)
        } else {
            NetworkResult.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        var parsedMessage: String?
        Util.parseBackendResponseError(
            e.response()?.errorBody()
        ).let { error: BackendResponseDto ->
            parsedMessage = BackendResponseDtoMapper().mapToDomainModel(error).message
        }
        if (parsedMessage.isNullOrEmpty()) {
            parsedMessage = e.message()
        }
        NetworkResult.Error(code = e.code(), message = parsedMessage)
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}
