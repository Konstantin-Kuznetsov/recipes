package com.example.feed_paging.domain.model

import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.RecipeItem

typealias RemoteUpdateDataResult = RecipesResult<RecipesUpdateData, RecipesError>
typealias CacheDataResult = RecipesResult<List<RecipeItem>, RecipesError>
typealias FeedDataResult = RecipesResult<RecipesFeedData, RecipesError>