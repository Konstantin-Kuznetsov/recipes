package com.example.feed.domain

import com.example.core.data.database.entities.RecipeEntity
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.DataSourceType
import com.example.core.domain.model.RecipeItem
import com.example.feed.domain.model.CacheDataResult
import com.example.feed.domain.model.RecipesUpdateData
import com.example.feed.domain.repo.RecipesFeedRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RecipesFeedInteractorImplTest {

    @Mock
    private lateinit var repo: RecipesFeedRepo

    private lateinit var interactor: RecipesFeedInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        interactor = RecipesFeedInteractorImpl(
            recipesFeedRepo = repo
        )
    }

    @Test
    fun getRecipesSuccessRemoteUpdate() {
        runBlocking(IO) {
            doReturn(successNonEmptyApiUpdateResult)
                .whenever(repo)
                .updateRecipesRemotely()

            doReturn(successNonEmptyCacheResult)
                .whenever(repo)
                .getCachedRecipes()

            val updateRecipesResult = interactor.getRecipes()

            verify(repo, times(1)).updateRecipesRemotely()
            verify(repo, times(1)).getCachedRecipes()

            assertTrue("Wrong getRecipes() result type", updateRecipesResult is RecipesResult.Data)
            assertTrue(
                "Wrong getRecipes() recipes list after mapping",
                (updateRecipesResult as RecipesResult.Data).value.recipes == nonEmptyDomainRecipes
            )
            assertTrue(
                "Wrong getRecipes() source type after success update",
                updateRecipesResult.value.dataSource == DataSourceType.Remote
            )

        }
    }

    @Test
    fun getRecipesFailRemoteUpdate() {
        runBlocking(IO) {
            doReturn(failApiUpdateResult)
                .whenever(repo)
                .updateRecipesRemotely()

            doReturn(successNonEmptyCacheResult)
                .whenever(repo)
                .getCachedRecipes()

            val updateRecipesResult = interactor.getRecipes()

            verify(repo, times(1)).updateRecipesRemotely()
            verify(repo, times(1)).getCachedRecipes()

            assertTrue("Wrong getRecipes() result type", updateRecipesResult is RecipesResult.Data)
            assertTrue(
                "Wrong getRecipes() recipes error after mapping",
                (updateRecipesResult as RecipesResult.Data).value.recipes ==
                        (successNonEmptyCacheResult as RecipesResult.Data).value
            )
            assertTrue(
                "Wrong getRecipes() source type",
                updateRecipesResult.value.dataSource == DataSourceType.CacheAfterError
            )
        }
    }

    @Test
    fun getRecipesSuccessEmptyRemoteUpdateWithCacheError() {
        runBlocking(IO) {
            doReturn(successEmptyApiUpdateResult)
                .whenever(repo)
                .updateRecipesRemotely()

            doReturn(cacheError)
                .whenever(repo)
                .getCachedRecipes()

            val updateRecipesResult = interactor.getRecipes()

            verify(repo, times(1)).updateRecipesRemotely()
            verify(repo, times(1)).getCachedRecipes()

            assertTrue("Wrong getRecipes() result type", updateRecipesResult is RecipesResult.Error)
            assertTrue(
                "Wrong getRecipes() error after mapping",
                (updateRecipesResult as RecipesResult.Error).error is RecipesError.CacheError
            )
        }
    }

    @Test
    fun reloadCachedResults() {
        // TODO
    }

    @Test
    fun updateRecipes() {
        // TODO
    }

    @Test
    fun updateIsFavouriteStatus() {
        // TODO
    }

    companion object {

        // API test data

        private val nonEmptyRecipeEntities = listOf(
            RecipeEntity("1", 1L, "1", "1", "1", "1", "1", "1", "1", "1", "1", 1, "1"),
            RecipeEntity("2", 2L, "2", "2", "2", "2", "2", "2", "2", "2", "2", 1, "2"),
            RecipeEntity("3", 3L, "3", "3", "3", "3", "3", "3", "3", "3", "3", 1, "3")
        )

        private val emptyRecipeEntities = emptyList<RecipeEntity>()

        val successNonEmptyApiUpdateResult: RecipesResult<RecipesUpdateData, RecipesError> =
            RecipesResult.Data(RecipesUpdateData(nonEmptyRecipeEntities))

        val successEmptyApiUpdateResult: RecipesResult<RecipesUpdateData, RecipesError> =
            RecipesResult.Data(RecipesUpdateData(emptyRecipeEntities))

        val failApiUpdateResult: RecipesResult<RecipesUpdateData, RecipesError> =
            RecipesResult.Error(RecipesError.NoInternetError)

        // cache test data

        private val emptyDomainRecipes = emptyList<RecipeItem>()

        private val cacheError: CacheDataResult = RecipesResult.Error(RecipesError.CacheError(RuntimeException()))

        private val nonEmptyDomainRecipes = listOf(
            RecipeItem(
                "1", 1L, "1", "1", "1", "1", "1", "1", "1", "1", "1", 1, "1",  true
            ),
            RecipeItem(
                "2", 2L, "2", "2", "2", "2", "2", "2", "2", "2", "2", 1, "2", true
            ),
            RecipeItem(
                "3", 3L, "3", "3", "3", "3", "3", "3", "3", "3", "3", 1, "3", true
            )
        )

        val successNonEmptyCacheResult: CacheDataResult = RecipesResult.Data(nonEmptyDomainRecipes)
    }
}