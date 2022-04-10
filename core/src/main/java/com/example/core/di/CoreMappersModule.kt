package com.example.core.di

import com.example.core.data.mapper.RecipeMapper
import com.example.core.data.mapper.RecipeMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreMappersModule {
    @Binds
    @Singleton
    abstract fun provideRecipeMapper(impl: RecipeMapperImpl): RecipeMapper
}