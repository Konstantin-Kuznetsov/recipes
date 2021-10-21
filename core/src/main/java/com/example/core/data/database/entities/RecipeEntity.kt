package com.example.core.data.database.entities

import androidx.room.*

@Entity(
    tableName = "recipes",
    indices = [Index(value = ["recipe_id"], unique = true)]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_id")
    val recipeId: String,
    @ColumnInfo(name = "last_update_millis")
    val lastUpdateMillis: Long,

    @ColumnInfo(name = "calories")
    val calories: String,
    @ColumnInfo(name = "carbos")
    val carbos: String,
    @ColumnInfo(name = "proteins")
    val proteins: String,
    @ColumnInfo(name = "fats")
    val fats: String,

    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "headline")
    val headline: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "thumb")
    val thumbUrl: String,
    @ColumnInfo(name = "image")
    val fullImageUrl: String,
    @ColumnInfo(name = "difficulty")
    val difficulty: Int,
    @ColumnInfo(name = "time")
    val time: String
)