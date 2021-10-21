package com.example.core.di

import android.app.Application
import android.content.Context
import com.example.core.data.database.RecipesDatabase
import com.example.core.data.database.dao.RecipesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideDatabase(): RecipesDao = RecipesDatabase.build(app).recipesDao()
}