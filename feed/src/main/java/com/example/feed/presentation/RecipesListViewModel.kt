package com.example.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.RecipesResult
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.domain.state.RecipesListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val interactor: RecipesFeedInteractor
) : ViewModel() {

    private val innerState = MutableStateFlow<RecipesListState>(RecipesListState.Loading)
    val recipesState = innerState.asStateFlow()

    fun loadRecipes() {
        innerState.value = RecipesListState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            when(val result = interactor.getRecipes()) {
                is RecipesResult.Error ->
                    innerState.value = RecipesListState.Error(result.error)
                is RecipesResult.Data -> {
                    innerState.value = RecipesListState.Data(result.value)
                }
            }
        }
    }
}