package com.example.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.FavouriteRecipeEntity
import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.database.entities.RecipeEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RecipesDatabaseTest {

    private lateinit var recipesDao: RecipesDao
    private lateinit var db: RecipesDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RecipesDatabase::class.java).build()
        recipesDao = db.recipesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // Replace recipes every time we reload data from API to ensure that we have the actual version of a recipe.
    // Testcase:
    // 1) insert items with same recipeId but different content. Also favInfo inserting in transaction under the hood
    // 2) check that result after last insert is last actual items with same recipeId.
    @Test
    fun reloadDataAndUpdateRecepies() {
        with(recipesDao) {
            insertRecipes(testRecipes)
            insertFavInfo(favInfos)
            insertRecipes(testRecipesModified)
            insertFavInfo(favInfos)
        }

        runBlocking(IO) {
            val resultData = recipesDao.getAllRecipes()
            assertEquals(resultData, fullRecipesModifiedItems)
        }
    }

    // Load data from API and check that "set favourite" feature works fine.
    // Testcase:
    // 1) Insert test items, all off them ae NOT selected as favourite recipe
    // 2) Set recipe with id=1 and and id=2 as favourite recipes
    // 3) Check that fav info was changed correctly
    @Test
    fun loadDataAndChangeFavInfo() {
        recipesDao.insertOrUpdateRecipesWithFavInfo(testRecipes)

        runBlocking(IO) {
            val startData = recipesDao.getAllRecipes()
            assertTrue(
                "Wrong insertion logic, items selected as favourite without reason",
                startData.all { !it.favInfo.isFavourite }
            )
        }

        recipesDao.updateFavInfo(FavouriteRecipeEntity(recipeId = "1", isFavourite = true, lastUpdateMillis =  111))
        recipesDao.updateFavInfo(FavouriteRecipeEntity(recipeId = "3", isFavourite = true, lastUpdateMillis =  111))

        runBlocking(IO) {
            val modifiedData = recipesDao.getAllRecipes()

            // id = 1, isFavourite = true
            assertTrue(
                "Wrong isFavourite logic, item with id = 1 should be with field isFavourite = true",
                modifiedData.first { it.recipe.recipeId == "1" }.favInfo.isFavourite ==  true
            )

            // id = 2, isFavourite = false
            assertTrue(
                "Wrong isFavourite logic, item with id = 2 selected as favourite without reason",
                modifiedData.first { it.recipe.recipeId == "2" }.favInfo.isFavourite == false
            )

            // id = 3, isFavourite = true
            assertTrue(
                "Wrong isFavourite logic, item with id = 3 should be with field isFavourite = true",
                modifiedData.first { it.recipe.recipeId == "3" }.favInfo.isFavourite == true
            )
        }
    }

    //---------------- test data ----------------

    companion object {
        private val recipe1 = RecipeEntity(
            recipeId = "1",
            lastUpdateMillis = 1L,
            calories = "1",
            carbos = "1",
            proteins = "1",
            fats = "1",
            name = "1",
            headline = "1",
            description = "1",
            thumbUrl = "1",
            fullImageUrl = "1",
            difficulty = 1,
            time = "1"
        )

        private val recipe2 = RecipeEntity(
            recipeId = "2",
            lastUpdateMillis = 2L,
            calories = "2",
            carbos = "2",
            proteins = "2",
            fats = "2",
            name = "2",
            headline = "2",
            description = "2",
            thumbUrl = "2",
            fullImageUrl = "2",
            difficulty = 2,
            time = "2"
        )

        private val recipe3 = RecipeEntity(
            recipeId = "3",
            lastUpdateMillis = 3L,
            calories = "3",
            carbos = "3",
            proteins = "3",
            fats = "3",
            name = "3",
            headline = "3",
            description = "3",
            thumbUrl = "3",
            fullImageUrl = "3",
            difficulty = 3,
            time = "3"
        )

        // modified items

        private val recipe1modified = RecipeEntity(
            recipeId = "1",
            lastUpdateMillis = 1L,
            calories = "111",
            carbos = "111",
            proteins = "111",
            fats = "111",
            name = "111",
            headline = "111",
            description = "111",
            thumbUrl = "111",
            fullImageUrl = "111",
            difficulty = 111,
            time = "111"
        )

        private val recipe2modified = RecipeEntity(
            recipeId = "2",
            lastUpdateMillis = 2L,
            calories = "222",
            carbos = "222",
            proteins = "222",
            fats = "222",
            name = "222",
            headline = "222",
            description = "222",
            thumbUrl = "222",
            fullImageUrl = "222",
            difficulty = 222,
            time = "222"
        )

        private val recipe3modified = RecipeEntity(
            recipeId = "3",
            lastUpdateMillis = 3L,
            calories = "333",
            carbos = "333",
            proteins = "333",
            fats = "333",
            name = "333",
            headline = "333",
            description = "333",
            thumbUrl = "333",
            fullImageUrl = "333",
            difficulty = 333,
            time = "333"
        )

        //fav info
        private val favInfo1 = FavouriteRecipeEntity(recipeId = "1", isFavourite = false, lastUpdateMillis = 111)
        private val favInfo2 = FavouriteRecipeEntity(recipeId = "2", isFavourite = false, lastUpdateMillis = 111)
        private val favInfo3 = FavouriteRecipeEntity(recipeId = "3", isFavourite = false, lastUpdateMillis = 111)

        private val favInfos = listOf(favInfo1, favInfo2, favInfo3)

        // full recipe items
        private val fullRecipe1Modified = FullRecipeInfo(
            recipe1modified,
            favInfo1
        )

        private val fullRecipe2Modified = FullRecipeInfo(
            recipe2modified,
            favInfo2
        )

        private val fullRecipe3Modified = FullRecipeInfo(
            recipe3modified,
            favInfo3
        )

        private val testRecipes: List<RecipeEntity> = listOf(recipe1, recipe2, recipe3)
        private val testRecipesModified: List<RecipeEntity> = listOf(recipe1modified, recipe2modified, recipe3modified)
        private val fullRecipesModifiedItems: List<FullRecipeInfo> = listOf(fullRecipe1Modified, fullRecipe2Modified,  fullRecipe3Modified)
    }
}