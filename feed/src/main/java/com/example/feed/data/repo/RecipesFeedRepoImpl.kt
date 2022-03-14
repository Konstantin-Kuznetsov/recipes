package com.example.feed.data.repo

import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.FavouriteRecipeEntity
import com.example.core.data.database.entities.RecipeEntity
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.data.mapper.RecipeMapper
import com.example.feed.domain.model.*
import com.example.feed.domain.repo.RecipesFeedRepo
import timber.log.Timber
import javax.inject.Inject

class RecipesFeedRepoImpl @Inject constructor(
    private val api: RecipesApi,
    private val cache: RecipesDao,
    private val mapper: RecipeMapper,
    private val errMapper: ApiResponseErrorMapper
) : RecipesFeedRepo {

    override suspend fun updateRecipesRemotely(): RemoteUpdateDataResult =
        runCatching {
            api.loadRecipes()
                .map(mapper::mapResponseToCache)
                .also(this::saveRecipesToCache) // caching for every success update
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

    override suspend fun updateIsFavouriteStatus(recipeId: String, newStatus: Boolean): Boolean =
        runCatching {
            cache.updateFavInfo(
                FavouriteRecipeEntity(
                    recipeId = recipeId,
                    isFavourite = newStatus,
                    lastUpdateMillis = System.currentTimeMillis()
                )
            )
        }.fold(
            onSuccess = { true },
            onFailure = {
                Timber.e("Updating IsFavourite status error for recipeId $recipeId: " + it.message)
                false
            }
        )

    private fun saveRecipesToCache(recipes: List<RecipeEntity>) {
        recipes.takeIf(List<RecipeEntity>::isNotEmpty)
            ?.let(cache::insertOrUpdateRecipesWithFavInfo)
    }
}