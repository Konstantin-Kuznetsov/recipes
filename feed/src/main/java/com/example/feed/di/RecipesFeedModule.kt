package com.example.feed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.mapper.RecipeMapper
import com.example.core.data.mapper.RecipeMapperImpl
import com.example.feed.data.repo.RecipesFeedRepoImpl
import com.example.feed.domain.RecipesFeedInteractorImpl
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.domain.repo.RecipesFeedRepo
import com.example.feed.presentation.RecipesListViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class RecipesFeedModule {

    @Provides
    fun provideRecipesFeedInteractor(
        feedRepo: RecipesFeedRepo
    ): RecipesFeedInteractor = RecipesFeedInteractorImpl(feedRepo)

    @Provides
    fun provideRecipesFeedRepo(
        api: RecipesApi,
        cache: RecipesDao,
        mapper: RecipeMapper,
        errorMapper: ApiResponseErrorMapper
    ): RecipesFeedRepo =
        RecipesFeedRepoImpl(api, cache, mapper, errorMapper)

    @Provides
    fun provideVmFactory(interactor: RecipesFeedInteractor): RecipesListViewModelFactory =
        RecipesListViewModelFactory(interactor)

    class RecipesListViewModelFactory @Inject constructor(
        private val interactor: RecipesFeedInteractor
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RecipesListViewModel(interactor) as T
    }

}