package com.example.details.presentation.state

sealed class RecipeDetailsEffect {
    object ErrorUpdatingFavStatus: RecipeDetailsEffect()
}
