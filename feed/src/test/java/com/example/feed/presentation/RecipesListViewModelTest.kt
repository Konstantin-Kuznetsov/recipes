package com.example.feed.presentation

import app.cash.turbine.test
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.DataSourceType
import com.example.core.domain.model.RecipeItem
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.domain.model.RecipesFeedData
import com.example.feed.presentation.state.RecipesListState
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RecipesListViewModelTest {

    @Mock
    private lateinit var interactor: RecipesFeedInteractor

    private lateinit var viewModel: RecipesListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this).close()
        viewModel = RecipesListViewModel(interactor)
    }

    @Test
    fun loadRecipesNonEmptyResult() {
        runTest {

            // non empty result for updating from API
            whenever(interactor.getRecipes()).thenReturn(nonEmptyResult)

            viewModel.recipesState.test {
                viewModel.loadRecipes()

                // initial state
                assertEquals(RecipesListState.Loading, awaitItem())

                // state with recipes data
                assertEquals(
                    RecipesListState.Data(
                        RecipesFeedData(
                            recipes = nonEmptyDomainRecipes,
                            dataSource = DataSourceType.Remote,
                            remoteUpdateError = null
                        )
                    ), awaitItem()
                )
                cancel()
            }
        }
    }

    @Test
    fun loadRecipesExceptionCase() {
        runTest {

            // exception during retrieving data from API
            whenever(interactor.getRecipes()).thenReturn(apiExceptionResult)

            viewModel.recipesState.test {
                viewModel.loadRecipes()

                // initial state
                assertEquals(RecipesListState.Loading, awaitItem())

                // state with exception
                assert((awaitItem() as RecipesListState.Error).err is RecipesError.NoInternetError)

                cancel()
            }
        }
    }

    private val nonEmptyDomainRecipes = listOf(
        RecipeItem(
            "1", 1L, "1", "1", "1", "1", "1", "1", "1", "1", "1", 1, "1", true
        ),
        RecipeItem(
            "2", 2L, "2", "2", "2", "2", "2", "2", "2", "2", "2", 1, "2", true
        ),
        RecipeItem(
            "3", 3L, "3", "3", "3", "3", "3", "3", "3", "3", "3", 1, "3", true
        )
    )

    private val nonEmptyResult = RecipesResult.Data<RecipesFeedData, RecipesError>(
        RecipesFeedData(
            recipes = nonEmptyDomainRecipes,
            dataSource = DataSourceType.Remote,
            remoteUpdateError = null
        )
    )

    private val apiExceptionResult = RecipesResult.Error<RecipesFeedData, RecipesError>(
        error = RecipesError.NoInternetError
    )
}