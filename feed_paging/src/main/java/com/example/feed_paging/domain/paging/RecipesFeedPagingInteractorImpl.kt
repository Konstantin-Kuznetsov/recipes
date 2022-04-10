package com.example.feed_paging.domain.paging

import androidx.paging.PagingSource
import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.DataSourceType
import com.example.feed_paging.domain.model.CacheDataResult
import com.example.feed_paging.domain.model.FeedDataResult
import com.example.feed_paging.domain.model.RecipesFeedData
import com.example.feed_paging.domain.model.RemoteUpdateDataResult
import com.example.feed_paging.domain.repo.RecipesPagingRepo
import timber.log.Timber
import javax.inject.Inject

interface RecipesPagingInteractor {
    suspend fun loadRecipes(): FeedDataResult
    fun getCachedResults(): PagingSource<Int, FullRecipeInfo>
}

class RecipesPagingInteractorImpl @Inject constructor(
    private val recipesFeedRepo: RecipesPagingRepo
): RecipesPagingInteractor {

    override suspend fun loadRecipes(): FeedDataResult {
        val updateResult = updateRecipes()

        return when (val cacheDataResult = getCachedRecipes()) {
            is RecipesResult.Error -> RecipesResult.Error(cacheDataResult.error)
            is RecipesResult.Data ->
                RecipesResult.Data(
                    RecipesFeedData(
                        recipes = cacheDataResult.value,
                        dataSource = if (updateResult is RecipesResult.Error) DataSourceType.CacheAfterError else DataSourceType.Remote,
                        remoteUpdateError = updateResult as? RecipesError
                    )
                )
        }
    }

    override fun getCachedResults(): PagingSource<Int, FullRecipeInfo> =
        recipesFeedRepo.getCachedRecipesPaging()

    private suspend fun updateRecipes(): RemoteUpdateDataResult =
        recipesFeedRepo.updateRecipesRemotely().also { result ->
            (result as? RecipesResult.Data)
                ?.let {
                    Timber.d("${it.value.recipes.size} recipes were updated successfully")
                } ?: run {
                val err = (result as RecipesResult.Error).error
                Timber.e("$err occurred while updating recipes data remotely")
            }
        }

    private suspend fun getCachedRecipes(): CacheDataResult = recipesFeedRepo.getCachedRecipes()
}
