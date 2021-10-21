package com.example.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.FavouriteRecipeEntity
import com.example.core.data.database.entities.RecipeEntity

/**
 * DB_VERSION = 1 initial
 */

@Database(entities = [RecipeEntity::class, FavouriteRecipeEntity::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

    companion object Builder {
        private const val Name = "recipes_db"

        fun build(context: Context) =
            Room.databaseBuilder(context, RecipesDatabase::class.java, Name)
                .fallbackToDestructiveMigration()
                .build()
    }
}