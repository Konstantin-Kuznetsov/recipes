package com.example.core.di

import android.content.Context
import com.example.core.data.database.RecipesDatabase
import com.example.core.data.database.dao.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): RecipesDao = RecipesDatabase.build(app).recipesDao()
}