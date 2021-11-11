package com.example.core.data.database.entities

import androidx.room.*

@Entity(
    tableName = "favourites",
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