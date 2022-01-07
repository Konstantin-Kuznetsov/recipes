package com.example.details.presentation.state

import com.example.core.data.model.RecipesError
import com.example.core.domain.model.RecipeItem

sealed class RecipeDetailsState {
    object Loading : RecipeDetailsState()
    data class Error(val err: RecipesError) : RecipeDetailsState()
    data class Data(val screenState: RecipeItem) : RecipeDetailsState()
}
