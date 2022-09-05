package com.zest.android.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.HolderFavoriteBinding
import com.zest.android.ui.favorite.FavoriteAdapter.FavoriteViewHolder
import kotlin.properties.Delegates

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * Favorite[Recipe] items into [RecyclerView] via [FavoriteViewHolder]
 *
 * Created by ZARA
 */
internal class FavoriteAdapter(private val listener: OnFavoriteFragmentInteractionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recipes: List<Recipe> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderFavoriteBinding = HolderFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(holderFavoriteBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FavoriteViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe = recipes[position]

    override fun getItemCount(): Int = recipes.size


    fun updateData(recipe: Recipe) {
        if (recipes.isNotEmpty() && recipes.contains(recipe)) {
            (recipes as MutableList<Recipe>).remove(recipe)
            notifyDataSetChanged()
        }
    }

    inner class FavoriteViewHolder(private val binding: HolderFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(recipe: Recipe) {
            binding.favoriteTitleTextView.text = recipe.title

            binding.favoriteImageView.load(recipe.image) {
                placeholder(R.color.whiteSmoke)
            }

            binding.favoriteIconImageView.setBackgroundResource(if (listener.isFavorited(recipe)) R.drawable.ic_star_full_vector
            else R.drawable.ic_star_empty_white_vector)

            binding.favoriteIconImageView.setOnClickListener {
                listener.showDeleteFavoriteDialog(recipe)
            }

            itemView.setOnClickListener {
                listener.gotoDetailPage(recipe)
            }
        }
    }
}
