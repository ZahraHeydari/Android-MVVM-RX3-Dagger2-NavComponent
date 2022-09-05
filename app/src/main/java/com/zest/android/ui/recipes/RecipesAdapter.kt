package com.zest.android.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.HolderRecipeBinding
import com.zest.android.ui.recipes.RecipesAdapter.RecipeViewHolder
import kotlin.properties.Delegates

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Recipe] items into [RecyclerView] via [RecipeViewHolder]
 *
 *
 * Created by ZARA
 */
internal class RecipesAdapter(private val listener: OnRecipesFragmentInteractionListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recipes: List<Recipe> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderRecipeBinding =
            HolderRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(holderRecipeBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecipeViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe = recipes[position]

    override fun getItemCount(): Int = recipes.size

    inner class RecipeViewHolder(private val binding: HolderRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(recipe: Recipe) {
            binding.recipeTitleTextView.text = recipe.title

            if (listener.isFavorited(recipe)) binding.recipeFavoriteImageView.setBackgroundResource(
                R.drawable.ic_star_full_vector
            )
            else binding.recipeFavoriteImageView.setBackgroundResource(R.drawable.ic_star_gray_empty_vector)

            binding.recipeFavoriteImageView.setOnClickListener {
                listener.addOrRemoveFavorites(recipe)
            }

            binding.recipeImageView.load(recipe.image) {
                placeholder(R.color.whiteSmoke)
            }

            itemView.setOnClickListener {
                listener.gotoDetailPage(recipe)
            }
        }
    }
}
