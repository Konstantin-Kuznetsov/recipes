package com.example.feed.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.model.RecipesResult
import com.example.feed_paging.domain.paging.RecipesPagingInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

@OptIn(ExperimentalPagingApi::class)
class RecipesRemoteMediator(
    private val recipesInteractor: RecipesPagingInteractor
) : RemoteMediator<Int, FullRecipeInfo>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FullRecipeInfo>
    ): MediatorResult =
        CoroutineScope(Dispatchers.IO + Job()).async {
            when (val result = recipesInteractor.loadRecipes()) {
                is RecipesResult.Error ->
                    MediatorResult.Error(result.error)
                is RecipesResult.Data -> {
                    // actually, here will be only one page because API with recipes doesn't support paging
                    MediatorResult.Success(endOfPaginationReached = true)
                }
            }
        }.await()
}