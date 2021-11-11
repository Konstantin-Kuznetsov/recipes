package com.example.feed.domain.state

sealed class RecipesListEffect {
    object ErrorUpdatingFavStatus: RecipesListEffect()
}
