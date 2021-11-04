package com.example.core.data.model

sealed class RecipesError : Exception() {
    object NoInternetError : RecipesError()
    object ApiError : RecipesError()
    data class CacheError(val err: Throwable) : RecipesError()
    data class UnknownError(val err: Throwable) : RecipesError()
}