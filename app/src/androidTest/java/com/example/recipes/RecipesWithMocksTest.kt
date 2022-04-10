package com.example.recipes

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.core.data.api.BaseUrlProvider
import com.example.core.data.api.FakeBaseUrlProviderImpl
import com.example.core.di.BaseUrlPoviderModule
import com.example.recipes.mockWebServer.RecipesMockResponseDispatcher
import com.example.recipes.screens.RecipeDetailsScreen
import com.example.recipes.screens.RecipesListScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.*
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@UninstallModules(BaseUrlPoviderModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RecipesWithMocksTest {

    private val mockWebServer = MockWebServer()
    private val responseDispatcher = RecipesMockResponseDispatcher()
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(8080)
        mockWebServer.dispatcher = responseDispatcher.SuccessDispatcher()
        testRule.launchActivity(Intent(targetContext, MainActivity::class.java))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class FakeBaseUrlModule {
        @Provides
        @Singleton
        fun bindUrlProvider(): BaseUrlProvider = FakeBaseUrlProviderImpl()
    }

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
        Waiting.sleep(4)

        val recipesList = RecipesListScreen()

        with(recipesList) {
            waitForRecipesReady()
            clickCardWithTextAndFavStatus(
                text = "Orange", // <-- Data from fake response
                isFavourited = false
            )
        }

        val recipeDetails = RecipeDetailsScreen()

        recipeDetails.clickFavouriteIcon() // set recipe favourited on details

        Espresso.pressBack()

        Waiting.sleep(3)

        recipesList.clickCardWithTextAndFavStatus( // click on card with favourited status
            text = "Orange",
            isFavourited = true
        )
    }
}