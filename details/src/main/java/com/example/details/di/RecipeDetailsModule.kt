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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RecipeDetailsModule {

    @Provides
    @ViewModelScoped
    fun provideRecipeDetailsInteractor(
        detailsRepo: RecipeDetailsRepo
    ): RecipeDetailsInteractor = RecipeDetailsInteractorImpl(detailsRepo)

    @Provides
    @ViewModelScoped
    fun provideRecipeDetailsRepo(
        cache: RecipesDao,
        mapper: RecipeMapper
    ): RecipeDetailsRepo =
        RecipeDetailsRepoImpl(cache, mapper)
}