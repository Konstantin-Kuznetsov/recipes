package com.example.core.domain.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface RecipesPagingSource {
    val recipesFlow: Flow<PagingData<RecipeItem>>
}