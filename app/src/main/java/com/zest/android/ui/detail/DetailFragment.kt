package com.zest.android.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.adroitandroid.chipcloud.ChipCloud
import com.adroitandroid.chipcloud.ChipListener
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.FragmentDetailBinding
import com.zest.android.ui.main.MainActivity
import com.zest.android.ui.recipes.RecipeViewModel
import javax.inject.Inject
import javax.inject.Provider

class DetailFragment : Fragment() {

    private var mRecipe: Recipe? = null
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: RecipeViewModel

    @Inject
    lateinit var viewModelProvider: Provider<RecipeViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).mainComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                viewModelProvider.get() as T
        })[RecipeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        arguments?.let {
            mRecipe = DetailFragmentArgs.fromBundle(it).recipe
            (activity as MainActivity).supportActionBar?.title = mRecipe?.title
            binding.detailInstructionsTextView.text = mRecipe?.instructions

            mRecipe?.let { nonNullRecipe ->
                checkIsFavorite(nonNullRecipe)

                binding.detailFab.setOnClickListener {
                    viewModel.addOrRemoveAsFavorite(nonNullRecipe)
                    checkIsFavorite(nonNullRecipe)
                }

                val tags = viewModel.loadTags(nonNullRecipe)
                if (tags.isNullOrEmpty()) binding.detailTagsContainer.visibility = View.GONE
                else {
                    ChipCloud.Configure()
                        .chipCloud(binding.detailTagChipCloud)
                        .labels(tags)
                        .mode(ChipCloud.Mode.SINGLE)
                        .allCaps(false)
                        // .gravity(ChipCloud.Gravity.CENTER)
                        .chipListener(OnChipListener(tags))
                        .build()
                }
            }

            binding.detailToolbarImageView.load(mRecipe?.image) {
                placeholder(R.color.whiteSmoke)
            }
        }

        return binding.root
    }

    private fun checkIsFavorite(nonNullRecipe: Recipe) {
        if (viewModel.isFavorited(nonNullRecipe)) binding.detailFab.setImageResource(R.drawable.ic_star_full_vector)
        else binding.detailFab.setImageResource(R.drawable.ic_star_empty_white_vector)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail, menu)
    }

    private inner class OnChipListener(private val tags: Array<String>) : ChipListener {

        override fun chipSelected(i: Int) {
            Log.d(TAG, "chipSelected() called with: i = [$i] Tags: ${tags[i]})")
            findNavController().navigate(R.id.searchFragment, bundleOf("text" to tags[i]))
        }

        override fun chipDeselected(i: Int) {
            Log.d(TAG, "chipDeselected() called with: i = [$i]")
        }
    }

    companion object {
        private val TAG = DetailFragment::class.java.name
    }
}
