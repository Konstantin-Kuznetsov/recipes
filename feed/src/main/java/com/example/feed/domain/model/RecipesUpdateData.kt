package com.example.feed.domain.model

import com.example.core.data.database.entities.RecipeEntity

data class RecipesUpdateData(
    val recipes: List<RecipeEntity>
)
