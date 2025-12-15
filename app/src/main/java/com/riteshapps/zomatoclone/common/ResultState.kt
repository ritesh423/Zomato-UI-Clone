package com.riteshapps.zomatoclone.common

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error<T>(val message: String) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()

}