package com.zest.android.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.HolderSearchBinding
import com.zest.android.ui.search.SearchAdapter.SearchViewHolder
import kotlin.properties.Delegates

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Recipe] items into [RecyclerView] via [SearchViewHolder]
 *
 * Created by ZARA
 */
class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recipes: List<Recipe> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderSearchBinding =
            HolderSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(holderSearchBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe = recipes[position]

    override fun getItemCount(): Int = recipes.size

    inner class SearchViewHolder(val binding: HolderSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(recipe: Recipe) {
            binding.searchTextView.text = recipe.title
            binding.searchImageView.load(recipe.image) {
                placeholder(R.color.whiteSmoke)
            }

            itemView.setOnClickListener {
                Navigation.findNavController(itemView)
                    .navigate(R.id.detailFragment, bundleOf("recipe" to recipe))
            }
        }
    }
}
