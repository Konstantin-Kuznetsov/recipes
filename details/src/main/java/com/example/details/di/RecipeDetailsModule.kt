package com.example.details.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.database.dao.RecipesDao
import com.example.core.data.mapper.RecipeMapper
import com.example.details.data.repo.RecipeDetailsRepoImpl
import com.example.details.domain.repo.RecipeDetailsRepo
import com.example.details.domain.state.RecipeDetailsInteractor
import com.example.details.domain.state.RecipeDetailsInteractorImpl
import com.example.details.presentation.RecipeDetailsViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class RecipeDetailsModule {

    @Provides
    fun provideRecipeDetailsInteractor(
        detailsRepo: RecipeDetailsRepo
    ): RecipeDetailsInteractor = RecipeDetailsInteractorImpl(detailsRepo)

    @Provides
    fun provideRecipeDetailsRepo(
        cache: RecipesDao,
        mapper: RecipeMapper
    ): RecipeDetailsRepo =
        RecipeDetailsRepoImpl(cache, mapper)

    @Provides
    fun provideVmFactory(interactor: RecipeDetailsInteractor): RecipeDetailsViewModelFactory =
        RecipeDetailsViewModelFactory(interactor)

    class RecipeDetailsViewModelFactory @Inject constructor(
        private val interactor: RecipeDetailsInteractor
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RecipeDetailsViewModel(interactor) as T
    }
}