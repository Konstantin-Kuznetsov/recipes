package com.example.feed.di

import com.example.feed.data.repo.RecipesFeedRepoImpl
import com.example.feed.domain.RecipesFeedInteractorImpl
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.domain.repo.RecipesFeedRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
abstract class RecipesFeedModule {

    @Binds
    @FragmentScoped
    abstract fun provideRecipesFeedInteractor(impl: RecipesFeedInteractorImpl): RecipesFeedInteractor

    @Binds
    @FragmentScoped
    abstract fun provideRecipesFeedRepo(impl: RecipesFeedRepoImpl): RecipesFeedRepo
}