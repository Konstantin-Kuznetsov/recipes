package com.example.core.di

import com.example.core.data.mapper.RecipeMapper
import com.example.core.data.mapper.RecipeMapperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreMappersModule {
    @Provides
    @Singleton
    fun provideRecipeMapper(): RecipeMapper = RecipeMapperImpl()
}