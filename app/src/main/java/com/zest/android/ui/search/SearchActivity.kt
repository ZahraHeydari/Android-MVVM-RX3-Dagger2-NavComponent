package com.zest.android.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.MainApplication
import com.zest.android.R
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.ActivitySearchBinding
import com.zest.android.di.component.MainComponent
import com.zest.android.ui.detail.DetailActivity
import com.zest.android.ui.recipes.RecipeViewModel
import javax.inject.Inject
import javax.inject.Provider

/**
 * @Author ZARA.
 */
class SearchActivity : LifecycleLoggingActivity(), OnSearchCallback {


    private var mQuery: String? = null
    private var mSearchView: SearchView? = null
    private var mAdapter = SearchAdapter(this)
    private var mMenuSearchItem: MenuItem? = null
    lateinit var mainComponent: MainComponent
    lateinit var binding: ActivitySearchBinding
    lateinit var viewModel: RecipeViewModel

    @Inject
    lateinit var viewModelProvider: Provider<RecipeViewModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainComponent = (applicationContext as MainApplication).provideAppComponent().mainComponent().create()
        mainComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    viewModelProvider.get() as T
        }).get(RecipeViewModel::class.java)

        setSupportActionBar(binding.searchToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.searchRecyclerView.adapter = mAdapter

        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onChanged() {
                super.onChanged()
                checkEmptyView()
            }
        })

        if (Action_SEARCH_TAG == intent?.action) {
            intent?.extras?.let {
                if (it.containsKey(String::class.java.name)) {
                    mQuery = it.getString(String::class.java.name)
                    mQuery?.let { nonNullSearchQuery ->
                        viewModel.search(nonNullSearchQuery)
                    }
                }
            }
        }

        viewModel.recipesData.observe(this, Observer {
            mAdapter.recipes = it
        })
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        mSearchView = MenuItemCompat.getActionView(menu.findItem(R.id.search)) as SearchView
        mMenuSearchItem = menu.findItem(R.id.search)
        mMenuSearchItem?.expandActionView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSearchView?.foregroundGravity = GravityCompat.RELATIVE_LAYOUT_DIRECTION
        }
        mSearchView?.setHorizontalGravity(Gravity.END)
        mSearchView?.queryHint = getString(R.string.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        mSearchView?.setOnQueryTextListener(OnSearchQueryTextListener())
        mSearchView?.setOnCloseListener(OnSearchCloseListener())

        if (Action_SEARCH_TAG == intent?.action) {
            mSearchView?.setQuery(mQuery, false)
        }
        mSearchView?.isIconified = false
        return true
    }

    override fun onBackPressed() {
        if (mSearchView != null) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    private fun checkEmptyView() {
        binding.searchEmptyContainer.visibility = if (mAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    private inner class OnSearchCloseListener : SearchView.OnCloseListener {
        override fun onClose(): Boolean {
            finish()
            return false
        }
    }

    private inner class OnSearchQueryTextListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            mQuery = query
            viewModel.search(mQuery ?: "")
            MenuItemCompat.expandActionView(mMenuSearchItem)
            return false
        }

        override fun onQueryTextChange(s: String): Boolean {
            return false
        }
    }


    companion object {


        private val TAG = SearchActivity::class.java.name
        private val Action_SEARCH_TAG = "com.zest.android.ACTION_SEARCH_TAG"


        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }

        fun startWithText(context: Context, text: String) {
            val starter = Intent(context, SearchActivity::class.java).apply {
                Bundle().apply {
                    putExtra(String::class.java.name, text)
                    putExtras(this)
                }
                action = Action_SEARCH_TAG
            }
            context.startActivity(starter)
        }
    }

}
