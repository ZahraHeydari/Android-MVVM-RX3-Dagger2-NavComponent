package com.zest.android.ui.category


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.zest.android.R
import com.zest.android.ui.category.CategoryAdapter.CategoryViewHolder
import com.zest.android.data.model.Category
import com.zest.android.databinding.HolderCategoryBinding
import kotlin.properties.Delegates

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Category] items into [RecyclerView] via [CategoryViewHolder]
 *
 * Created by ZARA
 */
class CategoryAdapter(private val listener: OnCategoryFragmentInteractionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var categories: List<Category> by Delegates.observable(emptyList()) { property, oldValue, newValue ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding = HolderCategoryBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CategoryViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Category = categories[position]


    override fun getItemCount() = categories.size


    private inner class CategoryViewHolder(private val binding: HolderCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(category: Category) {

            binding.categoryImageView.load(category.image) {
                placeholder(R.color.whiteSmoke)
            }

            binding.categoryTextView.text = category.title

            itemView.setOnClickListener {
                listener.showSubCategories(category)
            }
        }
    }
}