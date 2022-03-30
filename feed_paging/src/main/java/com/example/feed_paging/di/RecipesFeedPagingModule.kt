package com.example.feed_paging.di

import com.example.core.domain.model.RecipesPagingSource
import com.example.feed_paging.domain.paging.*
import com.example.feed_paging.domain.repo.RecipesPagingRepo
import com.example.feed_paging.domain.repo.RecipesPagingRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RecipesFeedPagingModule {

    @Binds
    @ViewModelScoped
    abstract fun provideRecipesPagingInteractor(impl: RecipesPagingInteractorImpl): RecipesPagingInteractor

    @Binds
    @ViewModelScoped
    abstract fun providePagingRepository(impl: RecipesPagingRepoImpl): RecipesPagingRepo

    @Binds
    @ViewModelScoped
    abstract fun provideRecipesPagingSource(impl: RecipesPagingSourceImpl): RecipesPagingSource

}