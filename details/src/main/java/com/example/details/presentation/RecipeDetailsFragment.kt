package com.example.details.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.core.app.RecipesApp
import com.example.details.R
import com.example.details.databinding.FragmentRecipeDetailsBinding
import com.example.details.di.DaggerRecipeDetailsComponent
import com.example.details.di.RecipeDetailsModule
import com.example.details.presentation.state.RecipeDetailsEffect
import com.example.details.presentation.state.RecipeDetailsState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_details) {

    @Inject
    lateinit var vmFactory: RecipeDetailsModule.RecipeDetailsViewModelFactory
    private val viewModel by viewModels<RecipeDetailsViewModel> { vmFactory }

    private val binding: FragmentRecipeDetailsBinding
        get() = requireNotNull(_viewBinding)

    private var _viewBinding: FragmentRecipeDetailsBinding? = null


    override fun onAttach(context: Context) {
        DaggerRecipeDetailsComponent
            .builder()
            .coreComponent(RecipesApp.coreComponent)
            .recipeDetailsModule(RecipeDetailsModule())
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // safe args are not implemented in this project, so we simply use the bundle
        arguments?.let {
            lifecycleScope.launch {
                viewModel.loadRecipe(arguments?.getString("RECIPE_ID").orEmpty())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentRecipeDetailsBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipesState.collect { renderState(it) }
            viewModel.effects.collect(::handleEffect)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun renderState(state: RecipeDetailsState) {
        when(state) {
            RecipeDetailsState.Loading -> {
                // todo
            }
            is RecipeDetailsState.Data -> showData(state)
            is RecipeDetailsState.Error -> {
                // todo
            }
        }
    }

    private fun handleEffect(effect: RecipeDetailsEffect) {
        // todo show toast
    }

    private fun showData(state: RecipeDetailsState.Data) {
        with(binding) {
            Glide.with(ivRecipeFull.context)
                .load(state.screenState.fullImageUrl)
                .into(ivRecipeFull)

            collapsingToolbar.title = state.screenState.name
            binding.contentScrolling.tvRecipeDescription.text = state.screenState.description

            fabIsFavourite.setImageResource(
                if (state.screenState.isFavourite) {
                    R.drawable.ic_favorited_red
                } else {
                    R.drawable.ic_not_favourited_border_black
                }
            )

            fabIsFavourite.setOnClickListener { viewModel.onFavouriteIconClick(state.screenState.recipeId, !state.screenState.isFavourite) }
        }
    }

    private fun showLoading() {
        // todo fullscreen loader
    }

    private fun showErrorScreen() {
        // TODO Not yet implemented
    }

    private fun showRecipesData() {
        // TODO Not yet implemented
    }

    private fun onRecipeItemClick(recipeId: String) {
        // TODO Not yet implemented
    }
}