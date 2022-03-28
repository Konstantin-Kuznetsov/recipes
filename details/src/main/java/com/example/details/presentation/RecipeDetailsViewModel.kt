package com.example.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.RecipesResult
import com.example.details.presentation.state.RecipeDetailsEffect
import com.example.details.domain.RecipeDetailsInteractor
import com.example.details.presentation.state.RecipeDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(
    private val interactor: RecipeDetailsInteractor
) : ViewModel() {

    private val innerState = MutableStateFlow<RecipeDetailsState>(RecipeDetailsState.Loading)
    val recipesState = innerState.asStateFlow()

    private val innerEffects = MutableSharedFlow<RecipeDetailsEffect>(replay = 0)
    val effects: SharedFlow<RecipeDetailsEffect>
        get() = innerEffects

    fun loadRecipe(recipeId: String) {
        innerState.value = RecipeDetailsState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = interactor.getCachedRecipe(recipeId)) {
                is RecipesResult.Error ->
                    innerState.value = RecipeDetailsState.Error(result.error)
                is RecipesResult.Data -> {
                    innerState.value = RecipeDetailsState.Data(result.value)
                }
            }
        }
    }

    fun onFavouriteIconClick(recipeId: String, isFavourite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            when (interactor.updateIsFavouriteStatus(recipeId, isFavourite)) {
                true -> reloadRecipeFromCache(recipeId)
                else -> innerEffects.tryEmit(RecipeDetailsEffect.ErrorUpdatingFavStatus)
            }
        }

    private fun reloadRecipeFromCache(recipeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = interactor.getCachedRecipe(recipeId)) {
                is RecipesResult.Error ->
                    innerState.value = RecipeDetailsState.Error(result.error)
                is RecipesResult.Data -> {
                    innerState.value = RecipeDetailsState.Data(result.value)
                }
            }
        }
    }
}