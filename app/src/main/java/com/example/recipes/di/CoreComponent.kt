package com.example.recipes.di

import android.content.Context
import com.example.recipes.data.database.dao.RecipesDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface CoreComponent {
    val context: Context
    val db: RecipesDao
}