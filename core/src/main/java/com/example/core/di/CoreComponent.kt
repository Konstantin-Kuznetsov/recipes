package com.example.core.di

import android.content.Context
import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class, NetworkModule::class])
interface CoreComponent {
    val context: Context
    val db: RecipesDao
    val api: RecipesApi
}