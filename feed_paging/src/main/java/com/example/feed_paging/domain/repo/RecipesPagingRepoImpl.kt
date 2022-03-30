package com.example.feed_paging.domain.repo

import androidx.paging.*
import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.database.entities.RecipeEntity
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.mapper.RecipeMapper
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.feed_paging.domain.model.CacheDataResult
import com.example.feed_paging.domain.model.RecipesUpdateData
import com.example.feed_paging.domain.model.RemoteUpdateDataResult
import javax.inject.Inject

interface RecipesPagingRepo {
    fun getCachedRecipesPaging(): PagingSource<Int, FullRecipeInfo>
    suspend fun getCachedRecipes(): CacheDataResult
    suspend fun updateRecipesRemotely(): RemoteUpdateDataResult
}

class RecipesPagingRepoImpl @Inject constructor(
    private val api: RecipesApi,
    private val cache: RecipesDao,
    private val errMapper: ApiResponseErrorMapper,
    private val mapper: RecipeMapper
) : RecipesPagingRepo {

    override fun getCachedRecipesPaging(): PagingSource<Int, FullRecipeInfo> =
        cache.getAllRecipesPaging()

    override suspend fun getCachedRecipes(): CacheDataResult =
        runCatching {
            cache.getAllRecipes()
                .map(mapper::mapCachedToDomain)
        }.fold(
            onSuccess = { RecipesResult.Data(it) },
            onFailure = { RecipesResult.Error(RecipesError.CacheError(it)) }
        )

    override suspend fun updateRecipesRemotely(): RemoteUpdateDataResult =
        runCatching {
            api.loadRecipes()
                .map(mapper::mapResponseToCache)
                .also(this::saveRecipesToCache) // caching for every success update
        }.fold(
            onSuccess = { RecipesResult.Data(RecipesUpdateData(it)) },
            onFailure = { RecipesResult.Error(errMapper.map(it)) }
        )

    private fun saveRecipesToCache(recipes: List<RecipeEntity>) {
        recipes.takeIf(List<RecipeEntity>::isNotEmpty)
            ?.let(cache::insertOrUpdateRecipesWithFavInfo)
    }
}