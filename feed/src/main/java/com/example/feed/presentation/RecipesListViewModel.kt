package com.example.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.RecipeItem
import com.example.core.domain.model.RecipesPagingSource
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.presentation.state.RecipesListEffect
import com.example.feed.presentation.state.RecipesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val interactor: RecipesFeedInteractor, // For "legacy" logic only
    private val recipesPagingSource: RecipesPagingSource
) : ViewModel() {

    private val innerState = MutableStateFlow<RecipesListState>(RecipesListState.Loading)
    val recipesState = innerState.asStateFlow()

    private val innerEffects = MutableSharedFlow<RecipesListEffect>(replay = 0)
    val effects: SharedFlow<RecipesListEffect>
        get() = innerEffects

    fun fetchRecipes(): Flow<PagingData<RecipeItem>> =
        recipesPagingSource.recipesFlow
            .cachedIn(viewModelScope)

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

    fun reloadRecipesFromCache() {
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