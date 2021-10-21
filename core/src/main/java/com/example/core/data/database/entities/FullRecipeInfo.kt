package com.example.core.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

/*
 Another option for joining data from two tables is custom SQL query and that's better for
 complex queries with some case-when-then logic, filtration or handling cursor manually.

 In this case @Embedded and @Relation annotations is what wee need, Room will automatically
 create two simple SQL queries under the hood and merge the result in FullRecipeInfo structure.
 And that's ok for simple relations between tables like in this case.
 */
data class FullRecipeInfo(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val favInfo: FavouriteRecipeEntity
)