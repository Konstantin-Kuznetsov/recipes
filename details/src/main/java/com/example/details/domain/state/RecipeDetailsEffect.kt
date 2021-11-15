package com.example.details.domain.state

sealed class RecipeDetailsEffect {
    object ErrorUpdatingFavStatus: RecipeDetailsEffect()
}
