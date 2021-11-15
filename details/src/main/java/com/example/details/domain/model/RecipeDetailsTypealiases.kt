package com.example.details.domain.model

import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.RecipeItem

typealias CachedRecipeResult = RecipesResult<RecipeItem, RecipesError>
