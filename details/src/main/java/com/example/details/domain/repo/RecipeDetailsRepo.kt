package com.example.details.domain.repo

import com.example.details.domain.model.CachedRecipeResult

interface RecipeDetailsRepo {
    suspend fun getCachedRecipe(recipeId: String): CachedRecipeResult
    suspend fun updateIsFavouriteStatus(recipeId: String, newStatus: Boolean): Boolean
}