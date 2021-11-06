package com.example.core.di

import android.content.Context
import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.mapper.RecipeMapper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class, NetworkModule::class, CoreMappersModule::class])
interface CoreComponent {
    val context: Context
    val db: RecipesDao
    val api: RecipesApi
    val recipeMapper: RecipeMapper
    val errMapper: ApiResponseErrorMapper
}