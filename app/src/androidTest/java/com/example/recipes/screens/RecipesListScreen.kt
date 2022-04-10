package com.example.recipes.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.recipes.*
import com.example.recipes.DrawableMatcher.Companion.imageWithDrawableId
import org.hamcrest.core.AllOf.allOf

class RecipesListScreen {

    private val swipeToRefreshLayout = withId(R.id.srlRecipesList)
    private val recipesList = withId(R.id.rvRecipes)

    fun waitForRecipesReady() = onView(recipesList).waitForVisible(20)

    // click on card with text in descendant view
    fun clickCardWithTextAndFavStatus(text: String, isFavourited: Boolean) =
        onView(recipesList).actionOnItem(
            allOf(
                ViewMatchers.hasDescendant(withText(text)),
                ViewMatchers.hasDescendant(
                    imageWithDrawableId(
                        if (isFavourited) {
                            R.drawable.ic_favorited_red
                        } else {
                            R.drawable.ic_not_favourited_border_black
                        }
                    )
                )
            ),
            ViewActions.click()
        )

    fun checkRecipeIsFavourited(text: String) =
        onView(recipesList).actionOnItem(
            ViewMatchers.hasDescendant(withText(text)),
            ViewActions.click()
        )
}