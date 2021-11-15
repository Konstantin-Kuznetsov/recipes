package com.example.details.data.repo

import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.FavouriteRecipeEntity
import com.example.core.data.mapper.RecipeMapper
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.details.domain.model.CachedRecipeResult
import com.example.details.domain.repo.RecipeDetailsRepo
import timber.log.Timber
import javax.inject.Inject

class RecipeDetailsRepoImpl @Inject constructor(
    private val cache: RecipesDao,
    private val mapper: RecipeMapper
) : RecipeDetailsRepo {

    override suspend fun getCachedRecipe(recipeId: String): CachedRecipeResult =
        runCatching {
            cache.getRecipeById(recipeId)
                .let(mapper::mapCachedToDomain)
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
}