package com.example.core.data.model

sealed class RecipesError : Exception() {
    object NoInternetError : RecipesError()
    object Unauthorized : RecipesError()
    data class ApiError(val err: Throwable) : RecipesError()
    data class CacheError(val err: Throwable) : RecipesError()
    data class UnknownError(val err: Throwable) : RecipesError()
}