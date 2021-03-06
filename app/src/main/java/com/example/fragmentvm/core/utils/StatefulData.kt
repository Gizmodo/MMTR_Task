package com.example.fragmentvm.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform

sealed class StatefulData<out T : Any> {
    data class Success<T : Any>(val result: T) : StatefulData<T>()

    // TODO: Waste it!
    data class Error(val message: String) : StatefulData<Nothing>()
    data class ErrorUiText(val message: UiText) : StatefulData<Nothing>()
    object Loading : StatefulData<Nothing>()

    inline fun <R : Any> map(transform: (T) -> R): StatefulData<R> {
        return when (this) {
            is Loading -> Loading
            is Error -> Error(this.message)
            is ErrorUiText -> ErrorUiText(this.message)
            is Success -> Success(transform(this.result))
        }
    }

    suspend inline fun <R : Any> suspendMap(
        crossinline transform: suspend (T) -> R
    ): StatefulData<R> {
        return when (this) {
            is Loading -> Loading
            is Error -> Error(this.message)
            is ErrorUiText -> ErrorUiText(this.message)
            is Success -> Success(transform(this.result))
        }
    }
}

fun <T : Any> Flow<T>.asStatefulData(): Flow<StatefulData<T>> = wrapWithStatefulData().catch {
    emit(StatefulData.Error(it.message ?: "Возникла ошибка"))
}

fun <T : Any> Flow<T>.wrapWithStatefulData(): Flow<StatefulData<T>> = transform { value ->
    return@transform emit(StatefulData.Success(value))
}

inline fun <T : Any, R : Any> Flow<StatefulData<T>>.mapState(
    crossinline transform: suspend (value: T) -> R
): Flow<StatefulData<R>> =
    transform { value ->
        return@transform emit(value.suspendMap(transform))
    }

inline fun <T : Any> Flow<StatefulData<T>>.onSuccessState(
    crossinline action: suspend (value: T) -> Unit
): Flow<StatefulData<T>> =
    onEach {
        if (it is StatefulData.Success) action(it.result)
    }

inline fun <T : Any> Flow<StatefulData<T>>.onErrorState(
    crossinline action: suspend (error: String) -> Unit
): Flow<StatefulData<T>> =
    onEach {
        if (it is StatefulData.Error) action(it.message)
    }
