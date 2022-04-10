package com.example.feed.di

import com.example.feed.data.repo.RecipesFeedRepoImpl
import com.example.feed.domain.RecipesFeedInteractorImpl
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.domain.repo.RecipesFeedRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RecipesFeedModule {

    @Binds
    @ViewModelScoped
    abstract fun provideRecipesFeedInteractor(impl: RecipesFeedInteractorImpl): RecipesFeedInteractor

    @Binds
    @ViewModelScoped
    abstract fun provideRecipesFeedRepo(impl: RecipesFeedRepoImpl): RecipesFeedRepo
}