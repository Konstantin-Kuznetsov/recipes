package com.example.recipes.data.api

import com.example.recipes.data.model.Recipe
import retrofit2.Response
import retrofit2.http.GET

interface RecipesApi {

    @GET("recipes.json")
    suspend fun loadRecipes(): Response<List<Recipe>>
}