package com.example.feed.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.RecipeItem
import com.example.feed.R
import com.example.feed.databinding.ItemRecipeBinding

class RecipesListPagingAdapter(
    private val cardClickListener: (String) -> Unit,
    private val isFavouriteClickListener: (String, Boolean) -> Unit
) : PagingDataAdapter<RecipeItem, RecipesListPagingAdapter.RecipeViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        getItem(position)?.let { recipeItem ->
            holder.bind(recipeItem, isFavouriteClickListener)
            holder.itemView.setOnClickListener {
                cardClickListener(recipeItem.recipeId)
            }
        }
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemRecipeBinding.bind(itemView)

        fun bind(
            recipe: RecipeItem,
            isFavouriteClickListener: (String, Boolean) -> Unit
        ) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(recipe.thumbUrl)
                    .into(ivRecipeThumb)

                tvRecipeHeader.text = recipe.name
                tvRecipeDescription.text = recipe.headline
                ivFavourite.background = if (recipe.isFavourite) {
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_favorited_red)
                } else {
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_not_favourited_border_black)
                }
                ivFavourite.setOnClickListener { isFavouriteClickListener(recipe.recipeId, !recipe.isFavourite) }
            }
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<RecipeItem>() {
    override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean =
        oldItem.recipeId == newItem.recipeId

    override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem): Boolean =
        oldItem == newItem
}