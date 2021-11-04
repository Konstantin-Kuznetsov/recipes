package com.example.feed.data.repo

import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.RecipeEntity
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.feed.data.mapper.RecipeMapper
import com.example.feed.domain.model.*
import com.example.feed.domain.repo.RecipesRepo
import javax.inject.Inject

class RecipesRepoImpl @Inject constructor(
    private val api: RecipesApi,
    private val cache: RecipesDao,
    private val mapper: RecipeMapper,
    private val errMapper: ApiResponseErrorMapper
) : RecipesRepo {

    override suspend fun updateRecipesRemotely(): RemoteUpdateDataResult =
        runCatching {
            api.loadRecipes()
                .map(mapper::mapResponseToCache)
                .also(this::saveRecipesToCache)
        }.fold(
            onSuccess = { RecipesResult.Data(RecipesUpdateData(it)) },
            onFailure = { RecipesResult.Error(errMapper.map(it)) }
        )

    override suspend fun getCachedRecipes(): CacheDataResult =
        runCatching {
            cache.getAllRecipes()
                .map(mapper::mapCachedToDomain)
        }.fold(
            onSuccess = { RecipesResult.Data(it) },
            onFailure = { RecipesResult.Error(RecipesError.CacheError(it)) }
        )

    private fun saveRecipesToCache(recipes: List<RecipeEntity>) {
        recipes.takeIf(List<RecipeEntity>::isNotEmpty)
            ?.let(cache::insertOrUpdateRecipesWithFavInfo)
    }
}