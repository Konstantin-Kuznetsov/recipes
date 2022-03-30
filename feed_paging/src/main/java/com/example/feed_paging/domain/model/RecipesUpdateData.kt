package com.example.feed_paging.domain.model

import com.example.core.data.database.entities.RecipeEntity

data class RecipesUpdateData(
    val recipes: List<RecipeEntity>
)
