package com.example.details.domain.state

import com.example.core.data.model.RecipesResult
import com.example.details.domain.model.CachedRecipeResult
import com.example.details.domain.repo.RecipeDetailsRepo
import javax.inject.Inject

interface RecipeDetailsInteractor {
    suspend fun getCachedRecipe(recipeId: String): CachedRecipeResult
    suspend fun updateIsFavouriteStatus(recipeId: String, newStatus: Boolean): Boolean
}

class RecipeDetailsInteractorImpl @Inject constructor(
    private val recipeDetailsRepo: RecipeDetailsRepo
) : RecipeDetailsInteractor {

    override suspend fun getCachedRecipe(recipeId: String): CachedRecipeResult =
        when (val cacheDataResult = recipeDetailsRepo.getCachedRecipe(recipeId)) {
            is RecipesResult.Error -> RecipesResult.Error(cacheDataResult.error)
            is RecipesResult.Data -> RecipesResult.Data(cacheDataResult.value)
        }

    override suspend fun updateIsFavouriteStatus(recipeId: String, newStatus: Boolean): Boolean =
        recipeDetailsRepo.updateIsFavouriteStatus(recipeId, newStatus)
}
