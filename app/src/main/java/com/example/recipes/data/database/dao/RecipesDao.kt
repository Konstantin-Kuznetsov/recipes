package com.example.recipes.data.database.dao

import androidx.room.*
import com.example.recipes.data.database.entities.FavouriteRecipeEntity
import com.example.recipes.data.database.entities.FullRecipeInfo
import com.example.recipes.data.database.entities.RecipeEntity

@Dao
interface RecipesDao {

    /*
     * Room runs two queries under the hood so we need to add @Transaction annotation.
     */
    @Transaction
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<FullRecipeInfo>

    /*
     * Replace recipes every time we reload data from API to ensure that we have the actual version of a recipe
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<RecipeEntity>): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavInfo(favInfo: List<FavouriteRecipeEntity>): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavInfo(favInfo: FavouriteRecipeEntity)
}