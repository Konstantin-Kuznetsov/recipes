package com.example.feed.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.core.app.RecipesApp
import com.example.feed.R
import com.example.feed.databinding.FragmentRecipesListBinding
import com.example.feed.di.DaggerRecipesFeedComponent
import com.example.feed.di.RecipesFeedModule
import com.example.feed.domain.state.RecipesListState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesListFragment : Fragment(R.layout.fragment_recipes_list) {

    @Inject
    lateinit var vmFactory: RecipesFeedModule.RecipesListViewModelFactory
    private val viewModel by viewModels<RecipesListViewModel> { vmFactory }

    private val binding: FragmentRecipesListBinding
        get() = requireNotNull(_viewBinding)

    private var _viewBinding: FragmentRecipesListBinding? = null

    override fun onAttach(context: Context) {
        DaggerRecipesFeedComponent
            .builder()
            .coreComponent(RecipesApp.coreComponent)
            .recipesFeedModule(RecipesFeedModule())
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.loadRecipes()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentRecipesListBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipesState.collect (::renderState)
        }
    }

    private fun renderState(state: RecipesListState) {
        when(state) {
            RecipesListState.Loading -> {
                // todo
            }
            is RecipesListState.Data -> {
                // todo
            }
            is RecipesListState.Error -> {
                // todo
            }
        }
    }

    private fun showLoading() {

    }

    private fun showErrorScreen() {

    }

    private fun showRecipesData() {

    }
}