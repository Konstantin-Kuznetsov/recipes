package com.example.core.data.api

import com.example.core.data.model.Recipe
import retrofit2.Response
import retrofit2.http.GET

interface RecipesApi {

    @GET("recipes.json")
    suspend fun loadRecipes(): Response<List<Recipe>>
}