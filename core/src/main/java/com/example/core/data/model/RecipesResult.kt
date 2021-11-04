package com.example.core.data.model

sealed class RecipesResult<D, E> {

    data class Data<D, E>(
        val value: D
    ) : RecipesResult<D, E>()

    data class Error<D, E>(
        val error: E
    ) : RecipesResult<D, E>()
}
