package com.example.feed.domain

import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.DataSourceType
import com.example.feed.domain.model.CacheDataResult
import com.example.feed.domain.model.FeedDataResult
import com.example.feed.domain.model.RecipesFeedData
import com.example.feed.domain.model.RemoteUpdateDataResult
import com.example.feed.domain.repo.RecipesRepo
import timber.log.Timber
import javax.inject.Inject

interface RecipesListInteractor {
    suspend fun getRecipes(): FeedDataResult
    suspend fun updateRecipes(): RemoteUpdateDataResult
}

class NotificationsListImpl @Inject constructor(
    private val recipesRepo: RecipesRepo
) : RecipesListInteractor {

    override suspend fun getRecipes(): FeedDataResult {
        val updateResult = updateRecipes()

        return when (val cacheDataResult = getCachedRecipes()) {
            is RecipesResult.Error -> RecipesResult.Error(cacheDataResult.error)
            is RecipesResult.Data ->
                RecipesResult.Data(
                    RecipesFeedData(
                        recipes = cacheDataResult.value,
                        dataSource = if (updateResult is RecipesError) DataSourceType.Cache else DataSourceType.Remote,
                        remoteUpdateError = updateResult as? RecipesError
                    )
                )
        }
    }

    override suspend fun updateRecipes(): RemoteUpdateDataResult =
        recipesRepo.updateRecipesRemotely().also { result ->
            (result as? RecipesResult.Data)
                ?.let {
                    Timber.d("${it.value.recipes.size} recipes were updated successfully")
                } ?: run {
                val err = (result as RecipesResult.Error).error
                Timber.e("$err occurred while updating recipes data remotely")
            }
        }

    private suspend fun getCachedRecipes(): CacheDataResult = recipesRepo.getCachedRecipes()
}
