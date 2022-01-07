package com.example.feed.presentation.state

import com.example.core.data.model.RecipesError
import com.example.feed.domain.model.RecipesFeedData

sealed class RecipesListState {
    object Loading : RecipesListState()
    data class Error(val err: RecipesError) : RecipesListState()
    data class Data(val screenState: RecipesFeedData) : RecipesListState()
}
