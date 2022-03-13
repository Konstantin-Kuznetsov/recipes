package com.example.feed.presentation

import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.DataSourceType
import com.example.core.domain.model.RecipeItem
import com.example.feed.domain.RecipesFeedInteractor
import com.example.feed.domain.model.RecipesFeedData
import com.example.feed.presentation.state.RecipesListState
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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

    @After
    fun tearDown() {
    }

    @Test
    fun loadRecipesNonEmptyResult() {
        runTest {

            // non empty result for updating from API
            whenever(interactor.getRecipes()).thenReturn(nonEmptyResult)

            val states = viewModel.recipesState.test(scope = this)

            // initial state
            assertEquals(viewModel.recipesState.value, RecipesListState.Loading)

            viewModel.loadRecipes()

            delay(500)

            // state after data successfully loaded
            assertEquals(
                viewModel.recipesState.value, RecipesListState.Data(
                    RecipesFeedData(
                        recipes = nonEmptyDomainRecipes,
                        dataSource = DataSourceType.Remote,
                        remoteUpdateError = null
                    )
                )
            )

            // finishing all the jobs
            states.finish()
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
}