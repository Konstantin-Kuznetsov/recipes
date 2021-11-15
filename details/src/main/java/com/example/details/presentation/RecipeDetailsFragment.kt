package com.example.details.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.core.app.RecipesApp
import com.example.details.R
import com.example.details.databinding.FragmentRecipeDetailsBinding
import com.example.details.di.DaggerRecipeDetailsComponent
import com.example.details.di.RecipeDetailsModule
import com.example.details.domain.state.RecipeDetailsEffect
import com.example.details.domain.state.RecipeDetailsState
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

    private fun renderState(state: RecipeDetailsState) {
        when(state) {
            RecipeDetailsState.Loading -> {
                // todo
            }
            is RecipeDetailsState.Data -> {
                // todo
            }
            is RecipeDetailsState.Error -> {
                // todo
            }
        }
    }

    private fun handleEffect(effect: RecipeDetailsEffect) {
        // todo show toast
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