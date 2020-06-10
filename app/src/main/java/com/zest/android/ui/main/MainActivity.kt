package com.zest.android.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.MainApplication
import com.zest.android.R
import com.zest.android.data.model.Category
import com.zest.android.data.model.Recipe
import com.zest.android.databinding.ActivityMainBinding
import com.zest.android.di.component.MainComponent
import com.zest.android.ui.category.CategoryFragment
import com.zest.android.ui.detail.DetailActivity
import com.zest.android.ui.recipes.RecipesFragment
import com.zest.android.ui.search.SearchActivity
import com.zest.android.util.extension.currentNavigationFragment
import com.zest.android.util.setupWithNavController

/**
 * @Author ZARA.
 */
class MainActivity : LifecycleLoggingActivity(), OnMainCallback {


    lateinit var mainComponent: MainComponent
    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creation of the main graph using the application graph
        mainComponent = (applicationContext as MainApplication).provideAppComponent().mainComponent().create()

        // Make Dagger instantiate @Inject fields in MainActivity
        mainComponent.inject(this)

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

        binding.mainToolbarTitleImageView.setOnClickListener{

            val fragmentById = supportFragmentManager.currentNavigationFragment
            if (fragmentById != null && fragmentById is RecipesFragment) fragmentById.scrollUp()
            else if (fragmentById is CategoryFragment) fragmentById.scrollUp()
        }

    }


    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
                R.navigation.recipes,
                R.navigation.category,
                R.navigation.favorites)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.mainBottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_fragment_container,
                intent = intent
        )
        currentNavController = controller
    }


    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_search) {
            SearchActivity.start(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    override fun gotoSubCategories(category: Category) {
        SearchActivity.startWithText(this@MainActivity, category.title)
    }

    override fun updateActionBarTitle(resId: Int) {
        supportActionBar?.setTitle(resId)
    }

    companion object {

        private val TAG = MainActivity::class.java.name

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
