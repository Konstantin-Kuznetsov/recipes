package com.example.core.data.mapper

import com.example.core.data.model.RecipesError
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface ApiResponseErrorMapper {
    fun map(err: Throwable): RecipesError
}

class ApiResponseErrorMapperImpl @Inject constructor() : ApiResponseErrorMapper {
    override fun map(err: Throwable): RecipesError =
        when {
            err is UnknownHostException -> RecipesError.NoInternetError
            (err as? HttpException)?.let { it.code() == 401 } ?: false -> RecipesError.Unauthorized
            (err as? HttpException)?.let { it.code() != 200 } ?: false -> RecipesError.ApiError(err)
            else -> RecipesError.UnknownError(err)
        }
}