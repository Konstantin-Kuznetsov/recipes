package com.example.feed.di

import com.example.core.di.CoreComponent
import com.example.feed.presentation.RecipesListFragment
import dagger.Component
import javax.inject.Scope

@RecipesFeedScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [
        RecipesFeedModule::class
    ]
)
interface RecipesFeedComponent {
    fun inject(fragment: RecipesListFragment)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RecipesFeedScope