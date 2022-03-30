package com.example.feed_paging.domain.paging

import androidx.paging.PagingData
import com.example.core.data.mapper.RecipeMapper
import com.example.core.domain.model.RecipeItem
import com.example.core.domain.model.RecipesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipesPagingSourceImpl @Inject constructor(
    val interactor: RecipesPagingInteractor,
    val mapper: RecipeMapper
) : RecipesPagingSource {
    override val recipesFlow: Flow<PagingData<RecipeItem>>
        get() = RecipesPagerFactory.create(interactor, mapper)
}