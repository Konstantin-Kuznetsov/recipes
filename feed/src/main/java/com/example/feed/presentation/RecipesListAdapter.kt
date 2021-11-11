package com.example.feed.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.RecipeItem
import com.example.feed.R
import com.example.feed.databinding.ItemRecipeBinding

class RecipesListAdapter(
    private val cardClickListener: (String) -> Unit,
    private val isFavouriteClickListener: (String, Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<RecipeItem>()

    private object ViewType {
        const val Recipe = 0
        // any other view types described here.
        // Another option is delegate adapter pattern (for example https://github.com/sockeqwe/AdapterDelegates)
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) =
        when (data[position]) {
            is RecipeItem -> ViewType.Recipe
            // other view types in RecyclerView
            else -> throw java.lang.IllegalArgumentException("Wrong view type here")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ViewType.Recipe -> RecipeViewHolder(parent.inflate(R.layout.item_recipe))
            else -> throw IllegalArgumentException("ViewHolder with type $viewType not exist")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecipeViewHolder -> holder.bind(data[position], isFavouriteClickListener)
        }
        holder.itemView.setOnClickListener {
            cardClickListener(data[position].recipeId)
        }
    }

    fun updateRecipesData(data: List<RecipeItem>) {
        val diffResult = DiffUtil.calculateDiff(
            RecipesDiffUtil(
                this.data,
                data
            )
        )
        this.data.clear()
        this.data.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
        LayoutInflater.from(context).inflate(layoutId, this, false)
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
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_favorite_black_24dp)
            } else {
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_favorite_border_black_24dp)
            }
            ivFavourite.setOnClickListener { isFavouriteClickListener(recipe.recipeId, !recipe.isFavourite) }
        }
    }
}

class RecipesDiffUtil(
    private val oldList: List<RecipeItem>,
    private val newList: List<RecipeItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].recipeId == newList[newItemPosition].recipeId
}
