package com.example.feed.data.repo

import com.example.core.data.api.RecipesApi
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.database.entities.FavouriteRecipeEntity
import com.example.core.data.database.entities.FullRecipeInfo
import com.example.core.data.database.entities.RecipeEntity
import com.example.core.data.mapper.ApiResponseErrorMapper
import com.example.core.data.mapper.ApiResponseErrorMapperImpl
import com.example.core.data.mapper.RecipeMapper
import com.example.core.data.mapper.RecipeMapperImpl
import com.example.core.data.model.Recipe
import com.example.core.data.model.RecipesError
import com.example.core.data.model.RecipesResult
import com.example.core.domain.model.RecipeItem
import com.example.feed.domain.repo.RecipesFeedRepo
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.lang.RuntimeException

class RecipesFeedRepoImplTest {

    @Mock
    private lateinit var mockApi: RecipesApi

    @Mock
    private lateinit var mockCache: RecipesDao

    @Spy
    private val mapper: RecipeMapper = RecipeMapperImpl()

    private val errMapper: ApiResponseErrorMapper = ApiResponseErrorMapperImpl()
    private lateinit var recipesFeedRepo: RecipesFeedRepo

    // Api
    private val emptyApiRecipes = emptyList<Recipe>()
    private val nonEmptyApiRecipes = listOf(
        Recipe(
            "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", 1, "1"
        ),
        Recipe(
            "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", 2, "1"

        ),
        Recipe(
            "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", 3, "3"
        )
    )

    // Cache
    private val emptyRecipes = emptyList<FullRecipeInfo>()
    private val nonEmptyRecipes = listOf(
        FullRecipeInfo(
            RecipeEntity("1", 1L, "1", "1", "1", "1", "1", "1", "1", "1", "1", 1, "1"),
            FavouriteRecipeEntity("1", true, 1L)
        ),
        FullRecipeInfo(
            RecipeEntity("2", 2L, "2", "2", "2", "2", "2", "2", "2", "2", "2", 1, "2"),
            FavouriteRecipeEntity("2", true, 2L)
        ),
        FullRecipeInfo(
            RecipeEntity("3", 3L, "3", "3", "3", "3", "3", "3", "3", "3", "3", 1, "3"),
            FavouriteRecipeEntity("3", true, 3L)
        )
    )

    // Domain
    private val emptyDomainRecipes = emptyList<RecipeItem>()
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

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this).close()
        recipesFeedRepo = RecipesFeedRepoImpl(
            api = mockApi,
            cache = mockCache,
            mapper = mapper,
            errMapper = errMapper
        )
    }

    @Test
    fun updateRecipesRemotelyNonEmptyRecipes() {
        runBlocking {
            whenever(mockApi.loadRecipes()).thenReturn(nonEmptyApiRecipes)

            val result = recipesFeedRepo.updateRecipesRemotely()

            verify(mapper, times(nonEmptyApiRecipes.size)).mapResponseToCache(any())
            verify(mockCache, times(1)).insertOrUpdateRecipesWithFavInfo(anyList())
            verify(mockApi, times(1)).loadRecipes()
            assert(result is RecipesResult.Data && result.value.recipes.size == nonEmptyApiRecipes.size)
        }
    }

    @Test
    fun updateRecipesRemotelyEmptyRecipes() {
        runBlocking {
            whenever(mockApi.loadRecipes()).thenReturn(emptyApiRecipes)

            val result = recipesFeedRepo.updateRecipesRemotely()

            verify(mapper, never()).mapResponseToCache(any())
            verify(mockCache, never()).insertOrUpdateRecipesWithFavInfo(anyList())
            verify(mockApi, times(1)).loadRecipes()
            assert(result is RecipesResult.Data && result.value.recipes.isEmpty())
        }
    }

    @Test
    fun updateRecipesRemotelyApiError() {
        runBlocking {
            whenever(mockApi.loadRecipes()).thenThrow(RuntimeException())

            val result = recipesFeedRepo.updateRecipesRemotely()

            verify(mapper, never()).mapResponseToCache(any())
            verify(mockCache, never()).insertOrUpdateRecipesWithFavInfo(anyList())
            verify(mockApi, times(1)).loadRecipes()
            assert(result is RecipesResult.Error && result.error is RecipesError.UnknownError)
        }
    }

    @Test
    fun getCachedRecipesNonEmptyResult() {
        runBlocking {
            whenever(mockCache.getAllRecipes()).thenReturn(nonEmptyRecipes)

            val result = recipesFeedRepo.getCachedRecipes()

            verify(mockCache, times(1)).getAllRecipes()
            verify(mapper, times(nonEmptyDomainRecipes.size)).mapCachedToDomain(any())
            assert(result is RecipesResult.Data && result.value == nonEmptyDomainRecipes)
        }
    }

    @Test
    fun getCachedRecipesEmptyResult() {
        runBlocking {
            whenever(mockCache.getAllRecipes()).thenReturn(emptyRecipes)

            val result = recipesFeedRepo.getCachedRecipes()

            verify(mockCache, times(1)).getAllRecipes()
            verify(mapper, never()).mapCachedToDomain(any())
            assert(result is RecipesResult.Data && result.value == emptyDomainRecipes)
        }
    }

    @Test
    fun getCachedRecipesCacheError() {
        runBlocking {
            whenever(mockCache.getAllRecipes()).thenThrow(RuntimeException())

            val result = recipesFeedRepo.getCachedRecipes()

            verify(mockCache, times(1)).getAllRecipes()
            verify(mapper, never()).mapCachedToDomain(any())
            assert(result is RecipesResult.Error && result.error is RecipesError.CacheError)
        }
    }

    @Test
    fun updateIsFavouriteStatusSuccess() {
        runBlocking {
            val isSuccess = recipesFeedRepo.updateIsFavouriteStatus(
                recipeId = "123",
                newStatus = true
            )

            verify(mockCache, times(1)).updateFavInfo(any())
            assert(isSuccess)
        }
    }

    @Test
    fun updateIsFavouriteStatusDbError() {
        runBlocking {
            whenever(mockCache.updateFavInfo(any())).thenThrow(RuntimeException())
            val isSuccess = recipesFeedRepo.updateIsFavouriteStatus(
                recipeId = "123",
                newStatus = true
            )

            verify(mockCache, times(1)).updateFavInfo(any())
            assert(!isSuccess)
        }
    }
}