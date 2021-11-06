package com.example.core.data.mapper

import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.database.entities.RecipeEntity
import com.example.core.data.model.Recipe
import com.example.core.domain.model.RecipeItem
import javax.inject.Inject

interface RecipeMapper {
    fun mapResponseToCache(response: Recipe): RecipeEntity

    fun mapCachedToDomain(cacheEntity: FullRecipeInfo): RecipeItem
}

class RecipeMapperImpl @Inject constructor() : RecipeMapper {
    override fun mapResponseToCache(response: Recipe): RecipeEntity =
        with(response) {
            RecipeEntity(
                lastUpdateMillis = System.currentTimeMillis(),
                recipeId = id,
                calories = calories,
                carbos = carbos,
                proteins = proteins,
                fats = fats,
                name = name,
                headline = headline,
                description = description,
                thumbUrl = thumbUrl,
                fullImageUrl = fullImageUrl,
                difficulty = difficulty,
                time = time
            )
        }

    override fun mapCachedToDomain(cacheEntity: FullRecipeInfo): RecipeItem =
        with(cacheEntity) {
            RecipeItem(
                lastUpdateMillis = recipe.lastUpdateMillis,
                recipeId = recipe.recipeId,
                calories = recipe.calories,
                carbos = recipe.carbos,
                proteins = recipe.proteins,
                fats = recipe.fats,
                name = recipe.name,
                headline = recipe.headline,
                description = recipe.description,
                thumbUrl = recipe.thumbUrl,
                fullImageUrl = recipe.fullImageUrl,
                difficulty = recipe.difficulty,
                time = recipe.time,
                isFavourite = favInfo.isFavourite
            )
        }
}