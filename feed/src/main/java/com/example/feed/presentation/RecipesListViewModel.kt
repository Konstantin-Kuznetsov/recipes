package com.example.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.RecipesResult
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.presentation.state.RecipesListEffect
import com.example.feed.presentation.state.RecipesListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val interactor: RecipesFeedInteractor
) : ViewModel() {

    private val innerState = MutableStateFlow<RecipesListState>(RecipesListState.Loading)
    val recipesState = innerState.asStateFlow()

    private val innerEffects = MutableSharedFlow<RecipesListEffect>(replay = 0)
    val effects: SharedFlow<RecipesListEffect>
        get() = innerEffects

    fun loadRecipes(fullscreenLoader: Boolean = false) {
        if (fullscreenLoader) {
            innerState.value = RecipesListState.Loading
        }

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = interactor.getRecipes()) {
                is RecipesResult.Error ->
                    innerState.value = RecipesListState.Error(result.error)
                is RecipesResult.Data -> {
                    innerState.value = RecipesListState.Data(result.value)
                }
            }
        }
    }

    fun onFavouriteIconClick(recipeId: String, isFavourite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            when (interactor.updateIsFavouriteStatus(recipeId, isFavourite)) {
                true -> reloadRecipesFromCache()
                else -> innerEffects.tryEmit(RecipesListEffect.ErrorUpdatingFavStatus)
            }
        }

    private fun reloadRecipesFromCache() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = interactor.reloadCachedResults()) {
                is RecipesResult.Error ->
                    innerState.value = RecipesListState.Error(result.error)
                is RecipesResult.Data -> {
                    innerState.value = RecipesListState.Data(result.value)
                }
            }
        }
    }
}