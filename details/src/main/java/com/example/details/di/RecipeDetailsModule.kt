package com.example.details.di

import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.mapper.RecipeMapper
import com.example.details.data.repo.RecipeDetailsRepoImpl
import com.example.details.domain.repo.RecipeDetailsRepo
import com.example.details.domain.RecipeDetailsInteractor
import com.example.details.domain.RecipeDetailsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class RecipeDetailsModule {

    @Provides
    @FragmentScoped
    fun provideRecipeDetailsInteractor(
        detailsRepo: RecipeDetailsRepo
    ): RecipeDetailsInteractor = RecipeDetailsInteractorImpl(detailsRepo)

    @Provides
    @FragmentScoped
    fun provideRecipeDetailsRepo(
        cache: RecipesDao,
        mapper: RecipeMapper
    ): RecipeDetailsRepo =
        RecipeDetailsRepoImpl(cache, mapper)
}