package com.example.feed_paging.domain.model

import com.example.core.data.model.RecipesError
import com.example.core.domain.model.DataSourceType
import com.example.core.domain.model.RecipeItem

data class RecipesFeedData(
    val recipes: List<RecipeItem>,
    val dataSource: DataSourceType,
    val remoteUpdateError: RecipesError? = null
)
