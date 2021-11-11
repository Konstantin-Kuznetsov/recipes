package com.example.feed.domain

import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.DataSourceType
import com.example.feed.domain.model.CacheDataResult
import com.example.feed.domain.model.FeedDataResult
import com.example.feed.domain.model.RecipesFeedData
import com.example.feed.domain.model.RemoteUpdateDataResult
import com.example.feed.domain.repo.RecipesFeedRepo
import timber.log.Timber
import javax.inject.Inject
/*
    suspend fun updateRecipes() can be useful in cases when we need to update recipes without UI interactions (from service?).
    Otherwise, this function should be private.
 */
interface RecipesFeedInteractor {
    suspend fun getRecipes(): FeedDataResult
    suspend fun reloadCachedResults(): FeedDataResult
    suspend fun updateRecipes(): RemoteUpdateDataResult
    suspend fun updateIsFavouriteStatus(recipeId: String, newStatus: Boolean): Boolean
}

class RecipesFeedInteractorImpl @Inject constructor(
    private val recipesFeedRepo: RecipesFeedRepo
) : RecipesFeedInteractor {

    override suspend fun getRecipes(): FeedDataResult {
        val updateResult = updateRecipes()

        return when (val cacheDataResult = getCachedRecipes()) {
            is RecipesResult.Error -> RecipesResult.Error(cacheDataResult.error)
            is RecipesResult.Data ->
                RecipesResult.Data(
                    RecipesFeedData(
                        recipes = cacheDataResult.value,
                        dataSource = if (updateResult is RecipesError) DataSourceType.CacheAfterError else DataSourceType.Remote,
                        remoteUpdateError = updateResult as? RecipesError
                    )
                )
        }
    }

    override suspend fun reloadCachedResults(): FeedDataResult =
        when (val cacheDataResult = getCachedRecipes()) {
            is RecipesResult.Error -> RecipesResult.Error(cacheDataResult.error)
            is RecipesResult.Data ->
                RecipesResult.Data(
                    RecipesFeedData(
                        recipes = cacheDataResult.value,
                        dataSource = DataSourceType.CacheAfterLocalUpdate
                    )
                )
        }

    override suspend fun updateRecipes(): RemoteUpdateDataResult =
        recipesFeedRepo.updateRecipesRemotely().also { result ->
            (result as? RecipesResult.Data)
                ?.let {
                    Timber.d("${it.value.recipes.size} recipes were updated successfully")
                } ?: run {
                val err = (result as RecipesResult.Error).error
                Timber.e("$err occurred while updating recipes data remotely")
            }
        }

    override suspend fun updateIsFavouriteStatus(recipeId: String, newStatus: Boolean): Boolean =
        recipesFeedRepo.updateIsFavouriteStatus(recipeId, newStatus)

    private suspend fun getCachedRecipes(): CacheDataResult = recipesFeedRepo.getCachedRecipes()
}
