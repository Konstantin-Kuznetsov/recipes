package com.example.details.di

import com.example.core.di.CoreComponent
import com.example.details.presentation.RecipeDetailsFragment
import dagger.Component
import javax.inject.Scope

@RecipeDetailsScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [
        RecipeDetailsModule::class
    ]
)
interface RecipeDetailsComponent {
    fun inject(fragment: RecipeDetailsFragment)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RecipeDetailsScope