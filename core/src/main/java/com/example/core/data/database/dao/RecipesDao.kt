package com.example.core.data.database.dao

import androidx.room.*
import com.example.core.data.database.entities.FavouriteRecipeEntity
import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.database.entities.RecipeEntity

@Dao
interface RecipesDao {

    /*
     * Room runs two queries under the hood so we need to add @Transaction annotation.
     */
    @Transaction
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<FullRecipeInfo>

    @Transaction
    open fun insertOrUpdateRecipesWithFavInfo(updatedRecipes: List<RecipeEntity>) {
        insertRecipes(updatedRecipes)
        updatedRecipes.map {
            FavouriteRecipeEntity(
                recipeId = it.recipeId,
                isFavourite = false, // default value for new recipes
                lastUpdateMillis = System.currentTimeMillis()
            )
        }.let(this::insertFavInfo)
    }

    /*
     * Replace recipes every time we reload data from API to ensure that we have the actual version of a recipe
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<RecipeEntity>)

    /*
     * Save favourite info for existing fields and create for new recipes
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavInfo(favInfo: List<FavouriteRecipeEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavInfo(favInfo: FavouriteRecipeEntity)
}