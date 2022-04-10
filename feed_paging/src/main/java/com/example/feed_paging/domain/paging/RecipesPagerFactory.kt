package com.example.feed_paging.domain.paging

import androidx.paging.*
import com.example.core.data.mapper.RecipeMapper
import com.example.core.domain.model.RecipeItem
import com.example.feed.domain.paging.RecipesRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal object RecipesPagerFactory {

    @OptIn(ExperimentalPagingApi::class)
    fun create(
        recipesPagingInteractor: RecipesPagingInteractor,
        mapper: RecipeMapper
    ): Flow<PagingData<RecipeItem>> =
        Pager(
            PagingConfig(pageSize = 5, enablePlaceholders = true, prefetchDistance = 1),
            remoteMediator = RecipesRemoteMediator(recipesPagingInteractor),
            pagingSourceFactory = { recipesPagingInteractor.getCachedResults() }
        ).flow.map { pagingData ->
            pagingData.map(mapper::mapCachedToDomain)
        }

}