package com.example.feed.domain.repo

import com.example.feed.domain.model.CacheDataResult
import com.example.feed.domain.model.RemoteUpdateDataResult

interface RecipesRepo {
    suspend fun updateRecipesRemotely(): RemoteUpdateDataResult
    suspend fun getCachedRecipes(): CacheDataResult
}