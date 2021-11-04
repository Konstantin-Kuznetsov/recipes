package com.example.core.domain.model

/*
    Domain model for recipe and "isFavourite" status
 */
data class RecipeItem(
    val recipeId: String,
    val lastUpdateMillis: Long,

    val calories: String,
    val carbos: String,
    val proteins: String,
    val fats: String,

    val name: String,
    val headline: String,
    val description: String,
    val thumbUrl: String,
    val fullImageUrl: String,
    val difficulty: Int,
    val time: String,

    val isFavourite: Boolean,
)