package com.example.feed.presentation.state

sealed class RecipesListEffect {
    object ErrorUpdatingFavStatus: RecipesListEffect()
}
