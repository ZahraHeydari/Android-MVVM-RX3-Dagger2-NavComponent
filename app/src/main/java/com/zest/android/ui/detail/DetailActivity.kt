package com.zest.android.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.adroitandroid.chipcloud.ChipCloud
import com.adroitandroid.chipcloud.ChipListener
import com.google.android.material.snackbar.Snackbar
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.MainApplication
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.ActivityDetailBinding
import com.zest.android.di.component.DetailComponent
import com.zest.android.ui.recipes.RecipeViewModel
import com.zest.android.ui.search.SearchActivity
import javax.inject.Inject
import javax.inject.Provider

/**
 * @Author ZARA.
 */
class DetailActivity : LifecycleLoggingActivity(), OnDetailCallback {


    private var mRecipe: Recipe? = null
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailComponent: DetailComponent
    private lateinit var viewModel: RecipeViewModel

    @Inject
    lateinit var viewModelProvider: Provider<RecipeViewModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        detailComponent = (applicationContext as MainApplication).provideAppComponent().detailComponent().create()
        detailComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    viewModelProvider.get() as T
        }).get(RecipeViewModel::class.java)


        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.detailToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        intent?.extras?.let {
            if (it.containsKey(Recipe::class.java.name)) {
                mRecipe = it.getParcelable(Recipe::class.java.name)
            }
        }

        mRecipe?.let { nonNullRecipe ->

            supportActionBar?.title = nonNullRecipe.title
            binding.detailInstructionsTextView.text = nonNullRecipe.instructions
            checkIsFavorite(nonNullRecipe)

            binding.detailToolbarImageView.load(nonNullRecipe.image) {
                placeholder(R.color.whiteSmoke)
            }

            binding.detailFab.setOnClickListener {
                viewModel.addOrRemoveAsFavorite(nonNullRecipe)
                checkIsFavorite(nonNullRecipe)
            }

            val tags = viewModel.loadTags(nonNullRecipe)
            if (tags.isNullOrEmpty()) {
                binding.detailTagsContainer.visibility = View.GONE
            } else {
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

    }

    private fun checkIsFavorite(nonNullRecipe: Recipe) {
        if (viewModel.isFavorited(nonNullRecipe)) binding.detailFab.setImageResource(R.drawable.ic_star_full_vector)
        else binding.detailFab.setImageResource(R.drawable.ic_star_empty_white_vector)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showMessage(stringRes: Int) {
        Snackbar.make(binding.detailCollapsingToolbarLayout, getString(stringRes),
                Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private inner class OnChipListener(private val tags: Array<String>) : ChipListener {

        override fun chipSelected(i: Int) {
            Log.d(TAG, "chipSelected() called with: i = [$i] Tags: ${tags[i]})")
            SearchActivity.startWithText(this@DetailActivity, tags[i])
        }

        override fun chipDeselected(i: Int) {
            Log.d(TAG, "chipDeselected() called with: i = [$i]")
        }
    }

    companion object {

        private val TAG = DetailActivity::class.java.name

        fun start(context: Context, recipe: Recipe) {
            val starter = Intent(context, DetailActivity::class.java).apply {
                putExtra(Recipe::class.java.name, recipe)
            }
            context.startActivity(starter)
        }
    }
}
