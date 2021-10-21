package com.example.recipes.data.database.entities

import androidx.room.*

@Entity(
    tableName = "favourites",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["recipe_id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["recipe_id"], unique = true)]
)
data class FavouriteRecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_id")
    val recipeId: String,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean,
    @ColumnInfo(name = "last_update_millis")
    val lastUpdateMillis: Long
)