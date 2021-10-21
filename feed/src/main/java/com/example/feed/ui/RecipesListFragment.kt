package com.example.feed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.feed.R
import com.example.feed.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment(R.layout.fragment_recipes_list) {

    private val viewBinding: FragmentRecipesListBinding
        get() = requireNotNull(_viewBinding)

    private var _viewBinding: FragmentRecipesListBinding? = null

    //private val viewModel: FragmentRecipesListViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentRecipesListBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.recipesListState
//                .catch { it.printStackTrace() }
//                .collect { renderState(it) }
        }
    }

//    private fun renderState(state: RecipesListState) {
//        with(viewBinding) {
//            // TODO
//        }
//    }
}