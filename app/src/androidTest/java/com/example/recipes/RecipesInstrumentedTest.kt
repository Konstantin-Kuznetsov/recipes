package com.example.recipes

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipes.screens.RecipeDetailsScreen
import com.example.recipes.screens.RecipesListScreen
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /*
        1) Waiting for data loaded on main screen
        2) Checking that item is not favourited yet
        3) Navigating to details screen by clicking the item card with given recipe header
        4) Clicking on "heart" button to add recipe to favourites on details screen
        5) Navigating back to the recipes list
        6) Checking that item is favourited in list after clicking on "heart" button on details
     */
    @Test
    fun loadDataAndSetRecipeAsFavourite() {
        Waiting.sleep(2)

        val recipesList = RecipesListScreen()

        with(recipesList) {
            waitForRecipesReady()
            clickCardWithTextAndFavStatus(
                text = "Moroccan Skirt Steak ",
                isFavourited = false
            )
        }

        val recipeDetails = RecipeDetailsScreen()

        recipeDetails.clickFavouriteIcon() // set recipe favourited on details

        Espresso.pressBack()

        Waiting.sleep(2)

        recipesList.clickCardWithTextAndFavStatus( // click on card with favourited status
            text = "Moroccan Skirt Steak ",
            isFavourited = true
        )
    }
}