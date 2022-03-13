package com.example.recipes.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.recipes.R

class RecipeDetailsScreen {

    private val favouriteIcon = withId(R.id.fabIsFavourite)

    fun clickFavouriteIcon() = onView(favouriteIcon).perform(ViewActions.click())
}