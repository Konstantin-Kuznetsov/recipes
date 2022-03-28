package com.example.feed.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.core.AppNavigator
import com.example.feed.R
import com.example.feed.databinding.FragmentRecipesListBinding
import com.example.feed.presentation.state.RecipesListEffect
import com.example.feed.presentation.state.RecipesListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipesListFragment : Fragment(R.layout.fragment_recipes_list) {

    @Inject
    lateinit var viewModel: RecipesListViewModel

    private val binding: FragmentRecipesListBinding
        get() = requireNotNull(_viewBinding)

    private var _viewBinding: FragmentRecipesListBinding? = null

    private val recipesAdapter by lazy {
        RecipesListAdapter(
            ::onRecipeItemClick,
            viewModel::onFavouriteIconClick
        )
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

        with(binding) {
            rvRecipes.adapter = recipesAdapter

            srlRecipesList.setOnRefreshListener {
                lifecycleScope.launch {
                    viewModel.loadRecipes()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipesState.collect(::renderState)
            viewModel.effects.collect(::handleEffect)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadRecipesFromCache()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun renderState(state: RecipesListState) {
        when(state) {
            RecipesListState.Loading -> {
                // todo
                binding.srlRecipesList.isRefreshing = false
            }
            is RecipesListState.Data -> {
                // todo
                binding.srlRecipesList.isRefreshing = false
                recipesAdapter.updateRecipesData(state.screenState.recipes)
            }
            is RecipesListState.Error -> {
                // todo
                binding.srlRecipesList.isRefreshing = false
            }
        }
    }

    private fun handleEffect(effect: RecipesListEffect) {
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
        (requireActivity() as? AppNavigator)?.toDetails(recipeId)
    }
}