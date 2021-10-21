package com.example.recipes.di

import android.app.Application
import android.content.Context
import com.example.recipes.data.database.RecipesDatabase
import com.example.recipes.data.database.dao.RecipesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideDatabase(): RecipesDao = RecipesDatabase.build(app).recipesDao()
}